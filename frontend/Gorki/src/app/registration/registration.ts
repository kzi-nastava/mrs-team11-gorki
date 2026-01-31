import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.html',
  styleUrls: ['./registration.css'],
})

export class Registration {

  isConfirmationSent: boolean = false;
  registeredEmail: string = '';
  imagePreview: string | ArrayBuffer | null = null;

  constructor(private router: Router, private cdr: ChangeDetectorRef) {}

  register(email: string, firstName: string, lastName: string, homeAddress: string, phoneNumber: string, password: string, confirmPassword: string) {
    if(!email){
      alert("Please enter a valid email address.");
      return;
    }
    if(!firstName || !lastName || !homeAddress || !phoneNumber || !password || !confirmPassword) {
      alert("Please fill in all required fields.");
      return;
    }
    this.registeredEmail = email;
    this.isConfirmationSent = true;
    
  }

  confirmEmail() {
    this.router.navigateByUrl('/');
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
      this.cdr.detectChanges();
    };

    reader.readAsDataURL(file);
  }
}