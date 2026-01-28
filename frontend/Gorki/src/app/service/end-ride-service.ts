import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../env/environment';
import { Observable } from 'rxjs';
import { FinishRideDto } from '../model/ui/finish-ride-dto';
import { FinishedRideDto } from '../model/ui/finished-ride-dto';


@Injectable({
  providedIn: 'root',
})
export class EndRideService {
  private api = environment.apiHost;

  constructor(private http: HttpClient) {}

  finishRide(driverId: number, data: FinishRideDto): Observable<FinishedRideDto> {
    return this.http.put<FinishedRideDto>(
      `${this.api}/drivers/${driverId}/rides/finish`,
      data
    );
    
  }
  
  getNextScheduledRide(driverId: number) {
    return this.http.get<any>(`${this.api}/drivers/${driverId}/rides/next`);
  }
}
