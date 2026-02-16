import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { LoginRequest } from '../infrastructure/model/login.model';
import { AuthService } from '../infrastructure/auth.service';
import { AuthResponse } from '../infrastructure/model/auth-response.model';
import { MapService } from '../map/map-service';
import { ResetPasswordService } from '../service/reset-password-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})

export class Login {

  @Output() close = new EventEmitter<void>();
  @Output() loggedIn = new EventEmitter<void>();

  isResetSent: boolean = false;
  resetEmail: string = '';

  constructor(private router: Router, private authService: AuthService, private resetPasswordService: ResetPasswordService,private mapService:MapService
  ) {}

  forgotPassword(email: string) {
    if(!email) { alert("Enter email"); return; }

    this.resetPasswordService.forgotPassword(email).subscribe({
      next: () => {
        this.resetEmail = "mrs.team11.gorki@gmail.com";
        this.isResetSent = true;
        alert("Reset link sent.");
      },
      error: (err) => alert(err?.error?.message ?? "Reset failed")

    });
  }

  login(email: string, password: string) {
    if(!email) {
      alert("Please enter your email address to login.");
      return;
    }
    if(!password) {
      alert("Please enter your password to login.");
      return;
    }
    //kada napises back, ovde izmene za login sa JWT
    const login: LoginRequest = {
      email: email,
      password: password
    }
    this.authService.login(login).subscribe({
      next: (response: AuthResponse) =>{
        localStorage.setItem('user', response.token);
        this.authService.setUser();

        this.loggedIn.emit();
        this.close.emit();
        this.router.navigateByUrl('/');
        
        const el = document.getElementById('estimation');
        this.mapService.clearAll();
        if (el) {
          el.style.filter = 'blur(0px)';
          el.style.pointerEvents = 'none';
          el.style.userSelect = 'none';
        }
      },
      error: (err) => {
        alert("Login failed. Check credentials.");
        console.error(err);
      }
    });
  }

  // goToReset(){
  //   this.close.emit();
  //   this.router.navigate(['/reset']);
  // }

  closeLogin() { 
    this.close.emit();
  }
  
}