import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {JwtHelperService} from '@auth0/angular-jwt';
import { AuthResponse } from './model/auth-response.model';
import { LoginRequest } from './model/login.model';

export interface RegisterRequestDTO {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: number;
  profileImage?: string | null;
}

@Injectable({ providedIn: 'root'})
export class AuthService {

  private userId: number = 0;
  private role: 'DRIVER' | 'PASSENGER' | 'ADMIN' | 'UNUSER' = 'UNUSER';

  getUserId() { return this.userId; }

  setAuth(userId: number, role: any) {
    this.userId = userId;
    this.role = role;
  }

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

  register(dto: RegisterRequestDTO): Observable<void> {
    return this.http.post<void>('/api/auth/register', dto);
  }
}
