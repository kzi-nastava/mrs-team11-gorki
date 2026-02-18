import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminRideMonitorDTO, GetDriverInfoDTO } from '../model/ui/admin-ride-monitor-dto';
import { environment } from '../../env/environment';

@Injectable({ providedIn: 'root' })
export class AdminRideMonitorService {
  private readonly baseUrl = `${environment.apiHost}`;

  constructor(private http: HttpClient) {}

  searchDriver(q: string): Observable<GetDriverInfoDTO> {
    const params = new HttpParams().set('q', q);
    return this.http.get<GetDriverInfoDTO>(`${this.baseUrl}/admin/drivers/search`, { params });
  }

  getActiveRide(driverId: number): Observable<AdminRideMonitorDTO> {
    return this.http.get<AdminRideMonitorDTO>(`${this.baseUrl}/admin/drivers/${driverId}/ride/active`);
  }
}
