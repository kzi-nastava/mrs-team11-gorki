import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../env/environment';
import { HomeVehicle } from '../model/ui/vehicle';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  constructor(private http: HttpClient) {}

    getAllVehicles(): Observable<HomeVehicle[]> {
      const url = `${environment.apiHost}/vehicles`;
  
      return this.http.get<HomeVehicle[]>(url);
    }
}
