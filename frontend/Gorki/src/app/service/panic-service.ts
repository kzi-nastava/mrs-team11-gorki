import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../env/environment';

@Injectable({ providedIn: 'root' })
export class PanicService {
  constructor(private http: HttpClient) {}

  triggerPanic(rideId: number): Observable<void> {
    return this.http.put<void>(`${environment.apiHost}/rides/${rideId}/panic`, {});
  }
}