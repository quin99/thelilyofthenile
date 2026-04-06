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
import { loadStripe, Stripe, StripeElements } from '@stripe/stripe-js';
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

  get total(): number {
    return this.placedOrder()?.totalAmount ?? 0;
  }

  async ngOnInit() {
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

      this.stripe = await loadStripe(environment.stripePublishableKey);
      if (!this.stripe) {
        this.error.set('Payment system unavailable. Please try again later.');
        this.loading.set(false);
        return;
      }

      this.elements = this.stripe.elements({
        clientSecret: intent.clientSecret,
        appearance: {
          theme: 'night',
          variables: {
            colorPrimary: '#c9a96e',
            colorBackground: '#261414',
            colorText: '#f5ede0',
            colorTextSecondary: '#b8a89a',
            colorDanger: '#e87b7b',
            fontFamily: 'Raleway, sans-serif',
            borderRadius: '0px',
            spacingUnit: '5px',
          },
          rules: {
            '.Input': { border: '1px solid #3a2020', backgroundColor: '#261414' },
            '.Input:focus': { border: '1px solid #c9a96e', boxShadow: 'none' },
            '.Label': { fontSize: '10px', letterSpacing: '0.1em', textTransform: 'uppercase' },
          },
        },
      });

      // Signal the template to render the #payment-element div
      this.loading.set(false);
      this.stripeReady.set(true);

      // afterNextRender fires after Angular has painted the DOM — guaranteed safe to mount
      afterNextRender(() => {
        const el = document.getElementById('payment-element');
        if (!el) {
          this.error.set('Mount error: #payment-element not found in DOM.');
          return;
        }
        try {
          const paymentElement = this.elements!.create('payment');
          paymentElement.mount(el);
          // Inspect the DOM 2 seconds after mount to see what Stripe injected
          setTimeout(() => {
            this.error.set(
              `el children=${el.children.length} | ` +
              `el height=${el.offsetHeight}px | ` +
              `el width=${el.offsetWidth}px | ` +
              `iframe=${el.querySelector('iframe') ? 'found' : 'NOT FOUND'}`
            );
          }, 2000);
        } catch (e: unknown) {
          this.error.set('Mount error: ' + (e instanceof Error ? e.message : String(e)));
        }
      }, { injector: this.injector });

    } catch (err: unknown) {
      console.error('Checkout init error:', err);
      const message = err instanceof Error ? err.message : 'An unexpected error occurred.';
      this.error.set(message);
      this.loading.set(false);
    }
  }

  async pay() {
    if (!this.stripe || !this.elements || this.processing()) return;

    this.processing.set(true);
    this.error.set(null);

    const { error, paymentIntent } = await this.stripe.confirmPayment({
      elements: this.elements,
      confirmParams: { return_url: `${window.location.origin}/` },
      redirect: 'if_required',
    });

    if (error) {
      this.error.set(error.message ?? 'Payment failed. Please try again.');
      this.processing.set(false);
      return;
    }

    if (paymentIntent?.status === 'succeeded') {
      await firstValueFrom(this.cartService.clearCart());
      this.success.set(true);
      this.processing.set(false);
    }
  }

  ngOnDestroy() {
    this.elements?.getElement('payment')?.destroy();
  }
}
