import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CartItem } from '../models/cart-item.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly base = `${environment.apiUrl}/v1/cart`;
  readonly itemCount = signal(0);

  constructor(private http: HttpClient) {}

  getCart(): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(this.base).pipe(
      tap(items => this.itemCount.set(items.reduce((sum, i) => sum + i.quantity, 0)))
    );
  }

  addItem(productId: number, quantity: number): Observable<CartItem> {
    return this.http.post<CartItem>(`${this.base}/add`, { productId, quantity }).pipe(
      tap(() => this.itemCount.update(n => n + quantity))
    );
  }

  removeItem(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/remove/${productId}`).pipe(
      tap(() => this.getCart().subscribe())
    );
  }

  clearCart(): Observable<void> {
    return this.http.delete<void>(`${this.base}/clear`).pipe(
      tap(() => this.itemCount.set(0))
    );
  }
}
