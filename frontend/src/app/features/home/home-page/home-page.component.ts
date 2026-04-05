import { AfterViewInit, ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
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

  featured = signal<Product[]>([]);

  testimonials = [
    { quote: '"The most beautiful arrangement I have ever received. It arrived perfectly wrapped and the blooms lasted over two weeks."', author: '— Melissa R., Brandon FL' },
    { quote: '"I ordered the gold lotus bracelet as a gift and she absolutely loved it. The packaging alone is worth it."',            author: '— Danielle T., Tampa FL' },
    { quote: '"Same day delivery came through perfectly. The roses were stunning — exactly what I envisioned for our anniversary."',   author: '— Priya S., Valrico FL' },
  ];

  accessories = [
    { icon: '◈', name: 'Gold Lotus Bracelet',  price: 48 },
    { icon: '✦', name: 'Pearl Bloom Cuff',      price: 65 },
    { icon: '❋', name: 'Floral Charm Set',      price: 34 },
    { icon: '☽', name: 'Nile Moon Pendant',     price: 52 },
  ];

  features = [
    { icon: '✿', label: 'Same Day Delivery', sub: 'Order before 2pm' },
    { icon: '❋', label: 'Handcrafted Daily',  sub: 'Fresh from our studio' },
    { icon: '✦', label: 'Gift Wrapping',      sub: 'Complimentary always' },
    { icon: '◈', label: 'Custom Orders',       sub: 'Bespoke arrangements' },
  ];

  ngOnInit() {
    this.productService.getAll().subscribe(products => this.featured.set(products.slice(0, 5)));
  }

  ngAfterViewInit() {
    const observer = new IntersectionObserver(
      entries => entries.forEach(e => {
        if (e.isIntersecting) { e.target.classList.add('visible'); observer.unobserve(e.target); }
      }),
      { threshold: 0.12 }
    );
    document.querySelectorAll('.reveal').forEach(el => observer.observe(el));
  }

  onAddToCart(productId: number) {
    this.cartService.addItem(productId, 1).subscribe();
  }
}
