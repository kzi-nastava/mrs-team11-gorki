import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateDriverDTO } from '../model/ui/create-driver-dto';
import { Observable } from 'rxjs';
import { CreatedDriverDTO } from '../model/ui/created-driver-dto';
import { environment } from '../../env/environment';

@Injectable({
  providedIn: 'root',
})
export class DriverRegistrationService {
  constructor(private http:HttpClient){}

  createDriver(dto:CreateDriverDTO): Observable<CreatedDriverDTO>{
    return this.http.post<CreatedDriverDTO>(`${environment.apiHost}/drivers`, dto);
  }
}
