import { Component, inject } from '@angular/core';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { ResetPasswordService } from '../service/reset-password-service';

@Component({
  selector: 'app-reset',
  imports: [],
  templateUrl: './reset.html',
  styleUrl: './reset.css',
})
export class Reset {
  private router = inject(Router);

  constructor(private route: ActivatedRoute, private resetPasswordService: ResetPasswordService) {}

  resetPassword(newPassword: string, confirmNewPassword: string) {
    const token = this.route.snapshot.queryParamMap.get('token');
    if(!token) { alert("Missing token"); return; }

    this.resetPasswordService.resetPassword({ token, newPassword, confirmNewPassword }).subscribe({
      next: () => { alert("Password changed"); this.router.navigate(['/']); },
      error: (err) => alert(err?.error?.message ?? "Reset failed")
    });
  }

}
