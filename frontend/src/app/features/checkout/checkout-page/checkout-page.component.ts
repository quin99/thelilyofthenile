import {
  afterNextRender,
  Component,
  inject,
  Injector,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { firstValueFrom } from 'rxjs';
import { loadStripe, Stripe, StripeCardElement, StripeElements } from '@stripe/stripe-js';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { PaymentService } from '../../../core/services/payment.service';
import { CartItem } from '../../../core/models/cart-item.model';
import { Order } from '../../../core/models/order.model';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './checkout-page.html',
})
export class CheckoutPageComponent implements OnInit, OnDestroy {
  private cartService = inject(CartService);
  private orderService = inject(OrderService);
  private paymentService = inject(PaymentService);
  private router = inject(Router);
  private injector = inject(Injector);

  items = signal<CartItem[]>([]);
  placedOrder = signal<Order | null>(null);
  loading = signal(true);
  processing = signal(false);
  error = signal<string | null>(null);
  success = signal(false);
  stripeReady = signal(false);

  private stripe: Stripe | null = null;
  private elements: StripeElements | null = null;
  private cardElement: StripeCardElement | null = null;
  private clientSecret = '';

  get total(): number {
    return this.placedOrder()?.totalAmount ?? 0;
  }

  async ngOnInit() {
    // Handle redirect return from Stripe (3DS, etc.)
    const params = new URLSearchParams(window.location.search);
    if (params.get('redirect_status') === 'succeeded') {
      await firstValueFrom(this.cartService.clearCart());
      this.loading.set(false);
      this.success.set(true);
      return;
    }

    try {
      const items = await firstValueFrom(this.cartService.getCart());
      this.items.set(items);

      if (items.length === 0) {
        this.router.navigate(['/cart']);
        return;
      }

      const order = await firstValueFrom(this.orderService.placeOrder());
      this.placedOrder.set(order);

      const intent = await firstValueFrom(this.paymentService.createPaymentIntent(order.id));
      this.clientSecret = intent.clientSecret;

      this.stripe = await loadStripe(environment.stripePublishableKey);
      if (!this.stripe) {
        this.error.set('Payment system unavailable. Please try again later.');
        this.loading.set(false);
        return;
      }

      this.elements = this.stripe.elements();

      this.loading.set(false);
      this.stripeReady.set(true);

      afterNextRender(() => {
        const el = document.getElementById('payment-element');
        if (!el || !this.elements) return;
        this.cardElement = this.elements.create('card', {
          style: {
            base: {
              color: '#f5ede0',
              fontFamily: '"Raleway", sans-serif',
              fontSize: '15px',
              fontSmoothing: 'antialiased',
              '::placeholder': { color: '#b8a89a' },
            },
            invalid: { color: '#e87b7b' },
          },
          hidePostalCode: true,
        });
        this.cardElement.mount(el);
      }, { injector: this.injector });

    } catch (err: unknown) {
      console.error('Checkout init error:', err);
      this.error.set(err instanceof Error ? err.message : 'An unexpected error occurred.');
      this.loading.set(false);
    }
  }

  async pay() {
if (!this.stripe || !this.cardElement || !this.clientSecret) {
      this.error.set('Payment not ready. Please refresh and try again.');
      return;
    }
    if (this.processing()) return;

    this.processing.set(true);
    this.error.set(null);

    try {
      const { error, paymentIntent } = await this.stripe.confirmCardPayment(
        this.clientSecret,
        { payment_method: { card: this.cardElement } }
      );

      if (error) {
        this.error.set(error.message ?? 'Payment failed. Please try again.');
        this.processing.set(false);
        return;
      }

      if (paymentIntent?.status === 'succeeded') {
        await firstValueFrom(this.cartService.clearCart());
        this.success.set(true);
        this.processing.set(false);
      } else {
        this.error.set(`Unexpected status: ${paymentIntent?.status ?? 'unknown'}. Please try again.`);
        this.processing.set(false);
      }
    } catch (e: unknown) {
      this.error.set('Payment error: ' + (e instanceof Error ? e.message : String(e)));
      this.processing.set(false);
    }
  }

  ngOnDestroy() {
    this.cardElement?.destroy();
  }
}
