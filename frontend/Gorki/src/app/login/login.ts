import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../infrastructure/auth.service';

type Role = 'admin' | 'driver' | 'user' | 'unuser';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})

export class Login {

  @Output() close = new EventEmitter<void>();
  @Output() loggedIn = new EventEmitter<Role>();

  isResetSent: boolean = false;
  resetEmail: string = '';
  role: Role = 'unuser';

  constructor(private router: Router) {}

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
    if(email === 'admin@gmail.com' && password === 'admin') {
      this.role='admin';
    } else if (email === 'driver@gmail.com' && password === 'driver') {
      this.role='driver';
    } else {
      this.role='user';
    } 

    //kada napises back, ovde izmene za login sa JWT

    this.loggedIn.emit(this.role);
    this.close.emit();
    this.router.navigateByUrl('/');

    const el = document.getElementById('estimation');

    if (el) {
      el.style.filter = 'blur(0px)';
      el.style.pointerEvents = 'auto';
      el.style.userSelect = 'auto';
    }
  }

  goToReset(){
    this.close.emit();
    this.router.navigate(['/reset']);
  }

  closeLogin() { 
    this.close.emit();
  }
  
}