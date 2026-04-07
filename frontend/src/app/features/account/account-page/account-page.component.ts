import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { OrderService } from '../../../core/services/order.service';
import { Order } from '../../../core/models/order.model';

@Component({
  selector: 'app-account-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, CurrencyPipe, DatePipe],
  templateUrl: './account-page.html',
})
export class AccountPageComponent implements OnInit {
  private auth = inject(AuthService);
  private orderService = inject(OrderService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  isLoggedIn = this.auth.isLoggedIn;
  tab = signal<'login' | 'register'>('login');
  error = signal('');
  loading = signal(false);
  registered = signal(false);

  orders = signal<Order[]>([]);
  ordersLoading = signal(false);
  expandedOrderId = signal<number | null>(null);

  loginForm = { email: '', password: '' };
  registerForm = { username: '', email: '', password: '' };

  ngOnInit() {
    if (this.auth.isLoggedIn()) {
      this.loadOrders();
    }
  }

  loadOrders() {
    this.ordersLoading.set(true);
    this.orderService.getOrders().subscribe({
      next: orders => { this.orders.set(orders); this.ordersLoading.set(false); },
      error: () => this.ordersLoading.set(false),
    });
  }

  toggleOrder(id: number) {
    this.expandedOrderId.set(this.expandedOrderId() === id ? null : id);
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }

  login() {
    this.error.set('');
    this.loading.set(true);
    this.auth.login(this.loginForm).subscribe({
      next: () => {
        const redirect = this.route.snapshot.queryParamMap.get('redirect');
        if (redirect) {
          this.router.navigate([`/${redirect}`]);
        } else {
          this.loadOrders();
        }
      },
      error: () => {
        this.error.set('Invalid email or password.');
        this.loading.set(false);
      },
    });
  }

  register() {
    this.error.set('');
    this.loading.set(true);
    this.auth.register(this.registerForm).subscribe({
      next: () => {
        this.registered.set(true);
        this.loginForm.email = this.registerForm.email;
        this.tab.set('login');
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Registration failed. Please try again.');
        this.loading.set(false);
      },
    });
  }
}
