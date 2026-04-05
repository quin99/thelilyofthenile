import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { CustomerRegister, CustomerResponse } from '../models/customer.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'auth_token';
  readonly isLoggedIn = signal(this.hasToken());

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }) {
    return this.http
      .post(`${environment.apiUrl}/v1/customers/login`, credentials, { responseType: 'text' })
      .pipe(tap(token => {
        localStorage.setItem(this.tokenKey, token);
        this.isLoggedIn.set(true);
      }));
  }

  register(data: CustomerRegister) {
    return this.http.post<CustomerResponse>(
      `${environment.apiUrl}/v1/customers/register`,
      data
    );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.isLoggedIn.set(false);
  }

  token(): string {
    return localStorage.getItem(this.tokenKey) ?? '';
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }
}
