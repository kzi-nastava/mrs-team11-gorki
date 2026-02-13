import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../env/environment';

export interface PriceConfig {
  priceForStandardVehicles: number;
  priceForLuxuryVehicles: number;
  priceForVans: number;
  pricePerKm: number;
}

@Injectable({ providedIn: 'root' })
export class PriceConfigService {
  private readonly baseUrl = `${environment.apiHost}/admin`;
  private readonly getUrl = `${this.baseUrl}/priceConfig`;
  private readonly putUrl = `${this.baseUrl}/changePriceConfig`;

  constructor(private http: HttpClient) {}

  getCurrentConfig(): Observable<PriceConfig> {
    return this.http.get<PriceConfig>(this.getUrl);
  }

  updateConfig(payload: PriceConfig): Observable<PriceConfig> {
    return this.http.put<PriceConfig>(this.putUrl, payload);
  }
}
