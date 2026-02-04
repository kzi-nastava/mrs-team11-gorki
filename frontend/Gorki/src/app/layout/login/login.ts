import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth.service';
import { LoginRequest } from '../../infrastructure/model/login.model';
import { AuthResponse } from '../../infrastructure/model/auth-response.model';

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

  constructor(private router: Router, private authService:AuthService
  ) {}

  forgotPassword(email: string) {
    if(!email) {
      alert("Please enter your email address to reset your password.");
      return;
    }
    this.resetEmail = email;
    this.isResetSent = true;
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

  goToReset(){
    this.router.navigate(['/reset']);
  }

  closeLogin() { 
    this.close.emit();
  }
}