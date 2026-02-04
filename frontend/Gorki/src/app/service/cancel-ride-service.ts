import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RideCancelRequestDTO {
  cancellationReason: string;
  cancelledBy: 'DRIVER' | 'PASSENGER';
  actorId: number;
}

export interface RideCancelResponseDTO {
  rideId: number;
  rideStatus: 'REQUESTED' | 'ACCEPTED' | 'STARTED' | 'FINISHED' | 'CANCELED';
  cancellationReason: string;
  cancelledBy: string;
}

@Injectable({ providedIn: 'root' })
export class CancelRideService {
  constructor(private http: HttpClient) {}

  cancelRide(rideId: number, dto: RideCancelRequestDTO): Observable<RideCancelResponseDTO> {
    return this.http.post<RideCancelResponseDTO>(`/api/rides/${rideId}/cancel`, dto);
  }
}
