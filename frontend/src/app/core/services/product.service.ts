import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Category, Product } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly base = `${environment.apiUrl}/v1/products`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.base);
  }

  getByCategory(category: Category): Observable<Product[]> {
    return this.http.get<Product[]>(this.base, { params: { category } });
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.base}/${id}`);
  }
}
