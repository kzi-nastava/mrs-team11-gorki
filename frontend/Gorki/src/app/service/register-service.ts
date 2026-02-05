import { Injectable } from '@angular/core';
import { RegisterRequestDTO } from '../model/ui/register-request-dto';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  constructor(private http: HttpClient) {}
  register(dto: RegisterRequestDTO): Observable<void> {
    return this.http.post<void>('/api/auth/register', dto);
  }
}
