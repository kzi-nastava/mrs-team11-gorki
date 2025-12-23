import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.html',
  styleUrls: ['./registration.css'],
})

export class Registration {

  isConfirmationSent: boolean = false;
  registeredEmail: string = '';

  constructor(private router: Router) {}

  register(email: string) {
    if(!email){
      alert("Please enter a valid email address.");
      return;
    }

    this.registeredEmail = email;
    this.isConfirmationSent = true;
    
  }

  confirmEmail() {
    this.router.navigateByUrl('/');
  }
}