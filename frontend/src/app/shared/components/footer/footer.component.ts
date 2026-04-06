import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink],
  templateUrl: './footer.component.html',
})
export class FooterComponent {
  year = new Date().getFullYear();

  footerColumns = [
    {
      heading: 'Shop',
      links: [
        { label: 'Arrangements', path: '/flowers' },
        { label: 'Bracelets',    path: '/bracelets' },
        { label: 'Trinkets',     path: '/trinkets' },
        { label: 'Seasonal',     path: '/seasonal' },
      ],
    },
    {
      heading: 'Account',
      links: [
        { label: 'Sign In',   path: '/account' },
        { label: 'Register',  path: '/account' },
        { label: 'My Orders', path: '/account' },
        { label: 'Cart',      path: '/cart' },
      ],
    },
    {
      heading: 'Info',
      links: [
        { label: 'Our Story',    path: '/' },
        { label: 'Care Guide',   path: '/' },
        { label: 'Custom Orders', path: '/' },
        { label: 'Contact',      path: '/' },
      ],
    },
  ];
}
