import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private key = 'auth_token';

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }) {
    return this.http
      .post<{ token: string }>(`${environment.apiUrl}/auth/login`, credentials)
      .pipe(tap(res => localStorage.setItem(this.key, res.token)));
  }
  logout() { localStorage.removeItem(this.key); }
  token() { return localStorage.getItem(this.key) ?? ''; }
  isLoggedIn() { return !!this.token(); }
}
