import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../env/environment';
import { RideInProgressDTO } from '../model/ui/ride-in-progress-dto';

@Injectable({
  providedIn: 'root',
})
export class DriverRideInProgress {
  constructor(private http: HttpClient) {}

  getActiveRide(driverId: number): Observable<RideInProgressDTO> {
    const url = `${environment.apiHost}/drivers/${driverId}/ride/active`;
    return this.http.get<RideInProgressDTO>(url);
  }

  getActiveRideAddresses(driverId: number): Observable<{pickup: string, dropoff: string}> {
    return this.getActiveRide(driverId).pipe(
      map(dto => {
        const locations = dto.route?.locations || [];
        return {
          pickup: locations[0]?.address || '',
          dropoff: locations[locations.length - 1]?.address || ''
        };
      })
    );
  }
}
