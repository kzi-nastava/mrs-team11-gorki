import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetRideDTO } from '../model/ui/get-ride-dto';
import { environment } from '../../env/environment';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class StartRideService {
  constructor(private http:HttpClient){}

  getNextScheduledRide(id:number): Observable<GetRideDTO | null>{
    return this.http.get<GetRideDTO>(`${environment.apiHost}/rides/${id}/next-ride`, {observe: 'response'}).pipe(map(res => res.status === 204 ? null : res.body));
  }

  startRide(id:number): Observable<GetRideDTO>{
    return this.http.put<GetRideDTO>(`${environment.apiHost}/rides/${id}/start`, {});
  }
}
