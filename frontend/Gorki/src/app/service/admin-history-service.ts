import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../env/environment';
import { PanicRide, Ride, UserHistoryRide } from '../rides/models/ride';

export interface UserOptionDTO {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

@Injectable({ providedIn: 'root' })
export class AdminHistoryService {

  constructor(private http: HttpClient) {}

  getPanicRides(from?: string, to?: string): Observable<UserHistoryRide[]> {
    const url = `${environment.apiHost}/admin/rides/panic`;

    const params: any = {};
    if (from) params.from = from;
    if (to) params.to = to;

    return this.http.get<any[]>(url, { params }).pipe(
      map(rides => rides.map(dto => this.mapRideDtoToRide(dto)))
    );
  }

  getAdminRides(from?: string, to?: string): Observable<UserHistoryRide[]> {
    const url = `${environment.apiHost}/admin/rides/history`;

    const params: any = {};
    if (from) params.from = from;
    if (to) params.to = to;

    return this.http.get<any[]>(url, { params }).pipe(
      map(rides => rides.map(dto => this.mapRideDtoToRide(dto)))
    );
  }

 private mapRideDtoToRide(dto: any): UserHistoryRide {
    const formatTime = (isoString: string) => {
      const date = new Date(isoString);
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${hours}:${minutes}`;
    };

    return {
      id: dto.rideId ?? dto.id,
      startTime: formatTime(dto.startingTime),
      endTime: formatTime(dto.endingTime),
      startLocation: dto.route?.locations[0]?.address || 'Nepoznata lokacija',
      destination: dto.route?.locations.slice(-1)[0]?.address || 'Nepoznata lokacija',
      date: new Date(dto.startingTime),
      price: dto.price,
      canceled: dto.canceled,
      canceledBy : dto.canceledBy || 'None',
      cancellationReason: dto.cancellationReason || 'None',
      panic: dto.panicActivated,
      passengers: dto.passengers || [],
      rating: dto.averageRating || null
    };
  }

  getUsers(): Observable<UserOptionDTO[]> {
    return this.http.get<UserOptionDTO[]>(`${environment.apiHost}/admin/users`);
  }

}