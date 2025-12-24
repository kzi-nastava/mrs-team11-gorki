import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';

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

  constructor(private router: Router) {}

  forgotPassword(email: string) {
    if(!email) {
      alert("Please enter your email address to reset your password.");
      return;
    }
    this.resetEmail = email;
    this.isResetSent = true;
  }

  login(){
    this.loggedIn.emit();
    this.close.emit();
    this.router.navigateByUrl('/');
  }

  goToReset(){
    this.router.navigate(['/reset']);
  }

  closeLogin() { 
    this.close.emit();
  }
}