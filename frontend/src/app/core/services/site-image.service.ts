import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SiteImageService {
  private http = inject(HttpClient);

  getAll(): Observable<Record<string, string>> {
    return this.http.get<Record<string, string>>('/api/v1/site-images');
  }

  upload(slot: string, file: File): Observable<{ slot: string; imageUrl: string }> {
    const formData = new FormData();
    formData.append('image', file);
    return this.http.put<{ slot: string; imageUrl: string }>(
      `/api/v1/admin/site-images/${slot}`,
      formData
    );
  }
}
