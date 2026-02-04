import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {JwtHelperService} from '@auth0/angular-jwt';
import { AuthResponse } from './model/auth-response.model';
import { LoginRequest } from './model/login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    skip: 'true',
  });

  private user$ = new BehaviorSubject("");
  userState = this.user$.asObservable();

  constructor(private http: HttpClient) {
    this.setUser();
  }

  login(auth: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('http://localhost:8080/api/auth/login', auth, {
      headers: this.headers,
    });
  }

  private getDecodedToken(): any | null {
    const token = localStorage.getItem('user');
    if (!token) return null;

    const helper = new JwtHelperService();
    return helper.decodeToken(token);
  }

  getRole(): string {
    return this.getDecodedToken()?.role ?? "";
  }

  getId(): number{
    return this.getDecodedToken()?.id ?? -1;
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('user') != null;
  }

  setUser(): void {
    this.user$.next(this.getRole());
  }
}
