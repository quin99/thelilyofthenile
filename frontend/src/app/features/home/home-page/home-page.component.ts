import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { SiteImageService } from '../../../core/services/site-image.service';
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-home-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './home-page.html',
})
export class HomePageComponent implements OnInit, AfterViewInit {
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  private authService = inject(AuthService);
  private siteImageService = inject(SiteImageService);
  private router = inject(Router);

  featured = signal<Product[]>([]);
  siteImages = signal<Record<string, string>>({});

  constructor() {
    effect(() => {
      if (this.featured().length > 0) {
        setTimeout(() => this.setupRevealObserver(), 0);
      }
    });
  }

  testimonials = [
    { quote: '"The most beautiful arrangement I have ever received. It arrived perfectly wrapped and the blooms lasted over two weeks."', author: '— Melissa R., Brandon FL' },
    { quote: '"I ordered the gold lotus bracelet as a gift and she absolutely loved it. The packaging alone is worth it."',            author: '— Danielle T., Tampa FL' },
    { quote: '"Same day delivery came through perfectly. The roses were stunning — exactly what I envisioned for our anniversary."',   author: '— Priya S., Valrico FL' },
  ];

  accessories = [
    { slot: 'accessory_1', imageUrl: 'images/jewelry1.jpg', name: 'Gold Lotus Bracelet',  price: 48 },
    { slot: 'accessory_2', imageUrl: 'images/jewelry2.jpg', name: 'Pearl Bloom Cuff',      price: 65 },
    { slot: 'accessory_3', imageUrl: 'images/jewelry3.jpg', name: 'Floral Charm Set',      price: 34 },
    { slot: 'accessory_4', imageUrl: 'images/jewelry4.jpg', name: 'Nile Moon Pendant',     price: 52 },
  ];

  features = [
    { icon: '✿', label: 'Same Day Delivery', sub: 'Order before 2pm' },
    { icon: '❋', label: 'Handcrafted Daily',  sub: 'Fresh from our studio' },
    { icon: '✦', label: 'Gift Wrapping',      sub: 'Complimentary always' },
    { icon: '◈', label: 'Custom Orders',       sub: 'Bespoke arrangements' },
  ];

  ngOnInit() {
    this.productService.getAll().subscribe(products => this.featured.set(products.slice(0, 5)));
    this.siteImageService.getAll().subscribe(images => this.siteImages.set(images));
  }

  imageFor(slot: string, fallback: string): string {
    return this.siteImages()[slot] ?? fallback;
  }

  ngAfterViewInit() {
    this.setupRevealObserver();
  }

  private setupRevealObserver() {
    const observer = new IntersectionObserver(
      entries => entries.forEach(e => {
        if (e.isIntersecting) { e.target.classList.add('visible'); observer.unobserve(e.target); }
      }),
      { threshold: 0.12 }
    );
    document.querySelectorAll('.reveal:not(.visible)').forEach(el => observer.observe(el));
  }

  onAddToCart(productId: number) {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/account'], { queryParams: { redirect: 'cart' } });
      return;
    }
    this.cartService.addItem(productId, 1).subscribe();
  }

  gridStyle(i: number): string {
    const styles = [
      'grid-column:1/6;grid-row:1/3;',
      'grid-column:6/9;grid-row:1/2;',
      'grid-column:9/13;grid-row:1/2;',
      'grid-column:6/10;grid-row:2/3;',
      'grid-column:10/13;grid-row:2/3;',
    ];
    return styles[i] ?? '';
  }
}
