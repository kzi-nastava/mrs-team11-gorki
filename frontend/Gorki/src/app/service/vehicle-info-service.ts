import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../env/environment';
import { GetVehicleDTO } from '../model/ui/get-vehicle-dto';
import { AuthService } from '../infrastructure/auth.service';
import { Vehicle } from '../model/ui/vehicle';

@Injectable({
  providedIn: 'root',
})
export class VehicleInfoService {
  constructor(private http: HttpClient){}

  getVehicleInfo(userId: number): Observable<GetVehicleDTO>{
    return this.http.get<any>(`${environment.apiHost}/drivers/${userId}/vehicle`);
  }

  updateVehicleInfo(userId: number, vehicle: Vehicle): Observable<GetVehicleDTO>{
    return this.http.put<GetVehicleDTO>(`${environment.apiHost}/drivers/${userId}/vehicle`, vehicle);
  }
}
