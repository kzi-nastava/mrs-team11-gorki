import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RideStopRequestDTO {
  latitude: number;
  longitude: number;
  address: string;
}

export interface RideStopResponseDTO {
  stopAddress: { latitude: number; longitude: number; address: string };
  endingTime: string;
  price: number;
}

@Injectable({ providedIn: 'root' })
export class StopRideService {
  constructor(private http: HttpClient) {}

  stopRide(rideId: number, dto: RideStopRequestDTO): Observable<RideStopResponseDTO> {
    return this.http.post<RideStopResponseDTO>(`/api/rides/${rideId}/stop`, dto);
  }
}
