import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private base = environment.apiUrl;
  constructor(private http: HttpClient) {}
  get<T>(url: string, opts?: any)  { return this.http.get<T>(`${this.base}${url}`, opts); }
  post<T>(url: string, body: any, opts?: any) { return this.http.post<T>(`${this.base}${url}`, body, opts); }
  del<T>(url: string, opts?: any)  { return this.http.delete<T>(`${this.base}${url}`, opts); }
  put<T>(url: string, body: any, opts?: any)  { return this.http.put<T>(`${this.base}${url}`, body, opts); }
}
