import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateRideDTO } from '../model/ui/create-ride-dto';
import { Observable } from 'rxjs';
import { CreatedRideDTO } from '../model/ui/created-ride-dto';
import { environment } from '../../env/environment';

@Injectable({
  providedIn: 'root',
})
export class OrderRideService {
  constructor(private http: HttpClient){}

  orderRide(dto: CreateRideDTO): Observable<CreatedRideDTO>{
    return this.http.post<CreatedRideDTO>(`${environment.apiHost}/rides`, dto);
  }
}
