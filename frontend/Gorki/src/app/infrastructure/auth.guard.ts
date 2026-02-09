import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  Router,
} from '@angular/router';
import {AuthService} from './auth.service';


@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
  ): boolean {
    const userRole: string = this.authService.getRole();
    const allowedRoles: string[] = route.data['role'] || [];
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['login']);
      return false;
    }
    if (!allowedRoles.includes(userRole)) {
      this.router.navigate(['HomePage']);
      return false;
    }
    return true;
  }
}
