import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterService } from '../service/register-service';
import { RegisterRequestDTO } from '../model/ui/register-request-dto';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.html',
  styleUrls: ['./registration.css'],
})
export class Registration {
  isConfirmationSent = false;
  registeredEmail = '';
  imagePreview: string | ArrayBuffer | null = null;
  selectedFile: File | null = null;

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private registerService: RegisterService
  ) {}

  confirmEmail() { this.router.navigateByUrl('/');}
  
  register(
    email: string,
    firstName: string,
    lastName: string,
    address: string,
    phoneNumber: string,
    password: string,
    confirmPassword: string
  ) {
    if (!email) {
      alert('Please enter a valid email address.');
      return;
    }

    if (!firstName || !lastName || !address || !phoneNumber || !password || !confirmPassword) {
      alert('Please fill in all required fields.');
      return;
    }

    if (password !== confirmPassword) {
      alert('Passwords do not match.');
      return;
    }

    const dto: RegisterRequestDTO = {
      email: email.trim(),
      password,
      confirmPassword,
      firstName: firstName.trim(),
      lastName: lastName.trim(),
      address: address.trim(),
      phoneNumber: Number(phoneNumber),
      profileImage: this.selectedFile ? this.selectedFile.name : null
    };

    this.registerService.register(dto).subscribe({
      next: () => {
        this.registeredEmail = dto.email;
        this.isConfirmationSent = true;

        alert('Registration successful! / Check your email for confirmation.');
        this.router.navigateByUrl('/');
      },
      error: (err) => {
        const msg = err?.error?.message ?? err?.error ?? 'Registration failed';
        alert(msg);
      }
    });
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];

    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
      this.cdr.detectChanges();
    };
    reader.readAsDataURL(file);
  }
}
