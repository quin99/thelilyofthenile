import { AfterViewInit, Component, HostListener } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavComponent } from './shared/components/nav/nav.component';
import { FooterComponent } from './shared/components/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavComponent, FooterComponent],
  template: `
    <div class="cursor" id="cursor"></div>
    <div class="cursor-ring" id="ring"></div>
    <app-nav />
    <main>
      <router-outlet />
    </main>
    <app-footer />
  `,
})
export class App implements AfterViewInit {
  private cursor!: HTMLElement;
  private ring!: HTMLElement;
  private observer!: IntersectionObserver;

  ngAfterViewInit() {
    this.cursor = document.getElementById('cursor')!;
    this.ring   = document.getElementById('ring')!;
    this.initRevealObserver();
  }

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(e: MouseEvent) {
    if (this.cursor) {
      this.cursor.style.left = e.clientX + 'px';
      this.cursor.style.top  = e.clientY + 'px';
    }
    if (this.ring) {
      this.ring.style.left = e.clientX + 'px';
      this.ring.style.top  = e.clientY + 'px';
    }
  }

  private initRevealObserver() {
    this.observer = new IntersectionObserver(
      entries => entries.forEach(e => {
        if (e.isIntersecting) {
          e.target.classList.add('visible');
          this.observer.unobserve(e.target);
        }
      }),
      { threshold: 0.15 }
    );
    document.querySelectorAll('.reveal').forEach(el => this.observer.observe(el));
  }
}
