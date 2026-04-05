import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-account-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule],
  templateUrl: './account-page.html',
})
export class AccountPageComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  tab = signal<'login' | 'register'>('login');
  error = signal('');
  loading = signal(false);

  loginForm = { email: '', password: '' };
  registerForm = { username: '', email: '', password: '' };

  login() {
    this.error.set('');
    this.loading.set(true);
    this.auth.login(this.loginForm).subscribe({
      next: () => this.router.navigate(['/']),
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
      next: () => this.tab.set('login'),
      error: () => {
        this.error.set('Registration failed. Please try again.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }
}
