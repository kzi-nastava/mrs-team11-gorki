import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NominatimItem {
  display_name: string;
  lat: string;
  lon: string;
}

@Injectable({
  providedIn: 'root',
})
export class NominatimService {
  constructor(private http: HttpClient) {}

  search(term: string, limit = 5): Observable<NominatimItem[]> {
    const params = new HttpParams()
      .set('q', term)
      .set('format', 'json')
      .set('addressdetails', '1')
      .set('limit', String(limit));

    // Direktno ka Nominatim-u (za dev)
    return this.http.get<NominatimItem[]>('https://nominatim.openstreetmap.org/search', { params });
  }
}
