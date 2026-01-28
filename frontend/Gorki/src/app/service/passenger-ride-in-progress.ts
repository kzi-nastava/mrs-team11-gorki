import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../env/environment';
import { RideInProgressDTO } from '../model/ui/ride-in-progress-dto';

@Injectable({ providedIn: 'root' })
export class RideInProgressService {

  constructor(private http: HttpClient) {}

  getActiveRide(passengerId: number): Observable<RideInProgressDTO> {
    const url = `${environment.apiHost}/passengers/${passengerId}/ride/active`;
    return this.http.get<RideInProgressDTO>(url);
  }

  getActiveRideAddresses(passengerId: number): Observable<{pickup: string, dropoff: string}> {
    return this.getActiveRide(passengerId).pipe(
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
