import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InconsistencyReportDTO } from '../model/ui/inconsistency-report-dto';
import { CreatedInconsistencyReportDTO } from '../model/ui/created-inconsistency-report-dto';
import { environment } from '../../env/environment';
@Injectable({
  providedIn: 'root',
})
export class InconsistencyService {

  private baseUrl = '/rides';

  constructor(private http: HttpClient) {}

  createReport(rideId: number, dto: InconsistencyReportDTO): Observable<CreatedInconsistencyReportDTO> {
    return this.http.post<CreatedInconsistencyReportDTO>(
      `${environment.apiHost}${this.baseUrl}/${rideId}/inconsistencies`,
      dto
    );
  }
}
