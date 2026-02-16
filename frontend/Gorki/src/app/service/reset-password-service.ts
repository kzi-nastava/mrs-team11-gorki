import { Injectable } from "@angular/core";
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../env/environment';
 
 
 @Injectable({
  providedIn: 'root',
})
export class ResetPasswordService {
 
    constructor(private http: HttpClient) {}
 
    forgotPassword(email: string) {
  return this.http.post<void>(
    `${environment.apiHost}/auth/forgot-password?email=${encodeURIComponent(email)}`,
    {}
  );
}

resetPassword(body: { token: string; newPassword: string; confirmNewPassword: string }) {
  return this.http.post<void>(`${environment.apiHost}/auth/reset-password`, body);
}
}