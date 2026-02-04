import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PanicService {
  constructor(private http: HttpClient) {}

  triggerPanic(rideId: number): Observable<void> {
    return this.http.put<void>(`/api/rides/${rideId}/panic`, {});
  }
}