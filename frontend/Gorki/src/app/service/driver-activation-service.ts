import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivateDriverRequestDTO } from '../model/ui/activate-driver-request-dto';
import { environment } from '../../env/environment';

@Injectable({
  providedIn: 'root',
})
export class DriverActivationService {
  constructor(private http:HttpClient){}

  activateDriver(dto:ActivateDriverRequestDTO){
    return this.http.post<void>(`${environment.apiHost}/auth/activate/driver`, dto);
  }
}
