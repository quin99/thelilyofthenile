import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PaymentIntentResponse {
  clientSecret: string;
  paymentIntentId: string;
}

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly base = `${environment.apiUrl}/v1/payments`;

  constructor(private http: HttpClient) {}

  createPaymentIntent(orderId: number): Observable<PaymentIntentResponse> {
    return this.http.post<PaymentIntentResponse>(`${this.base}/create-intent`, { orderId });
  }
}
