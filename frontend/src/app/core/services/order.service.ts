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

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${environment.apiUrl}/v1/admin/orders`);
  }

  updateStatus(id: number, status: string): Observable<Order> {
    return this.http.put<Order>(`${environment.apiUrl}/v1/admin/orders/${id}/status`, { status });
  }
}
