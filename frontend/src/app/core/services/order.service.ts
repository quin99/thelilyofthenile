import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Order } from '../models/order.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly base = `${environment.apiUrl}/v1/orders`;

  constructor(private http: HttpClient) {}

  placeOrder(): Observable<Order> {
    return this.http.post<Order>(`${this.base}/place`, {});
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.base);
  }
}
