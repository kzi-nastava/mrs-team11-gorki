import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReportDTO } from '../model/ui/report-dto';
import { environment } from '../../env/environment';
import { AuthService } from '../infrastructure/auth.service';
import { formatDate } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  constructor(private http:HttpClient, private auth:AuthService){}

  generatePassengerReport(from?:Date | null, to?:Date | null): Observable<ReportDTO>{
    const params: any = {};
    if (from) params.from = formatDate(from, 'yyyy-MM-dd', 'en-US');
    if (to) params.to = formatDate(to, 'yyyy-MM-dd', 'en-US');
    return this.http.get<ReportDTO>(`${environment.apiHost}/users/passenger/${this.auth.getId()}/report`, {params});
  }

  generateDriverReport(from?:Date | null, to?:Date | null): Observable<ReportDTO>{
    const params: any = {};
    if (from) params.from = formatDate(from, 'yyyy-MM-dd', 'en-US');
    if (to) params.to = formatDate(to, 'yyyy-MM-dd', 'en-US');
    return this.http.get<ReportDTO>(`${environment.apiHost}/users/driver/${this.auth.getId()}/report`, {params});
  }

  generateAggregateReport(from?:Date | null, to?:Date | null): Observable<ReportDTO>{
    const params: any = {};
    if (from) params.from = formatDate(from, 'yyyy-MM-dd', 'en-US');
    if (to) params.to = formatDate(to, 'yyyy-MM-dd', 'en-US');
    return this.http.get<ReportDTO>(`${environment.apiHost}/users/all/report`, {params});
  }

  generateAdminUserReport(email:string, from?:Date | null, to?:Date | null): Observable<ReportDTO>{
    const params: any = {};
    params.email = email;
    if (from) params.from = formatDate(from, 'yyyy-MM-dd', 'en-US');
    if (to) params.to = formatDate(to, 'yyyy-MM-dd', 'en-US');
    return this.http.get<ReportDTO>(`${environment.apiHost}/users/report`, {params});
  }
  
}
