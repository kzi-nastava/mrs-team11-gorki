import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetRideDTO } from '../model/ui/get-ride-dto';
import { environment } from '../../env/environment';

@Injectable({
  providedIn: 'root',
})
export class StartRideService {
  constructor(private http:HttpClient){}

  startRide(id:number): Observable<GetRideDTO>{
    return this.http.post<GetRideDTO>(`${environment.apiHost}/rides/${id}/start-ride`, {});
  }
}
