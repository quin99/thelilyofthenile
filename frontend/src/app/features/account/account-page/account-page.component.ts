import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  private route = inject(ActivatedRoute);

  tab = signal<'login' | 'register'>('login');
  error = signal('');
  loading = signal(false);
  registered = signal(false);

  loginForm = { email: '', password: '' };
  registerForm = { username: '', email: '', password: '' };

  login() {
    this.error.set('');
    this.loading.set(true);
    this.auth.login(this.loginForm).subscribe({
      next: () => {
        const redirect = this.route.snapshot.queryParamMap.get('redirect');
        this.router.navigate([redirect ? `/${redirect}` : '/']);
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
