import { ChangeDetectionStrategy, Component, HostListener, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './nav.component.html',
})
export class NavComponent {
  auth = inject(AuthService);
  cart = inject(CartService);
  scrolled = signal(false);
  menuOpen = signal(false);

  navLinks = [
    { label: 'Arrangements', path: '/flowers' },
    { label: 'Bracelets',    path: '/bracelets' },
    { label: 'Trinkets',     path: '/trinkets' },
    { label: 'Seasonal',     path: '/seasonal' },
  ];

  @HostListener('window:scroll')
  onScroll() {
    this.scrolled.set(window.scrollY > 40);
  }
}
