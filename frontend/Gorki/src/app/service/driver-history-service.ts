import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../env/environment';
import { Ride } from '../rides/models/ride';

@Injectable({ providedIn: 'root' })
export class DriverHistoryService {

  constructor(private http: HttpClient) {}

  getDriverRides(driverId: number, from?: string, to?: string): Observable<Ride[]> {
    let url = `${environment.apiHost}/drivers/${driverId}/rides/history`;

    const params: any = {};
    if (from) params.from = from;
    if (to) params.to = to;

    return this.http.get<any[]>(url, { params }).pipe(
      map(rides => rides.map(dto => this.mapRideDtoToRide(dto)))
    );
  }

 private mapRideDtoToRide(dto: any): Ride {
  const formatTime = (isoString: string) => {
    const date = new Date(isoString);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
  };

  return {
    id: dto.rideId,
    startTime: formatTime(dto.startingTime),
    endTime: formatTime(dto.endingTime),
    startLocation: dto.route?.locations[0]?.address || 'Nepoznata lokacija',
    destination: dto.route?.locations.slice(-1)[0]?.address || 'Nepoznata lokacija',
    date: new Date(dto.startingTime),
    price: dto.price,
    canceled: dto.canceled,
    panic: dto.panicActivated,
    passengers: dto.passengers || [],
    rating: 0 // ako backend još ne vraća ocenu
  };
}
}