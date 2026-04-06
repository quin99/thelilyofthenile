import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink],
  template: `
    <div class="pt-16 min-h-screen flex items-center justify-center px-6">
      <div class="text-center">
        <p class="text-[10px] tracking-[0.4em] uppercase text-brand-gold mb-4">404</p>
        <h1 class="font-display text-brand-ivory text-4xl mb-4">Page Not Found</h1>
        <p class="text-brand-ivory-dim mb-10 max-w-sm mx-auto">
          The page you're looking for has moved or doesn't exist.
        </p>
        <a routerLink="/"
           class="px-10 py-3.5 border border-brand-gold text-brand-gold text-xs tracking-widest uppercase
                  hover:bg-brand-gold hover:text-brand-bg transition-all duration-200">
          Return Home
        </a>
      </div>
    </div>
  `,
})
export class NotFoundComponent {}
