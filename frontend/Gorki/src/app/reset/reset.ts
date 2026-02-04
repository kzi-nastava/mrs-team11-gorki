import { Component, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';

@Component({
  selector: 'app-reset',
  imports: [],
  templateUrl: './reset.html',
  styleUrl: './reset.css',
})
export class Reset {
  private router = inject(Router);

  resetPassword(newPassword: string, confirmNewPassword: string) {
    if (!newPassword) {
      alert("Please enter your new password.");
      return;
    }
    if (!confirmNewPassword) {
      alert("Please confirm your new password.");
      return;
    }
    if (newPassword !== confirmNewPassword) {
      alert("Passwords do not match. Please try again.");
      return;
    }
    this.router.navigate(['/']);
  }
}
