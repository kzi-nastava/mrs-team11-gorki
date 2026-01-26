import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
  
  getRole(): string | null {
    //console.log(localStorage.getItem('role'))
    return localStorage.getItem('role'); // ADMIN, DRIVER, PASSENGER
  }
}
