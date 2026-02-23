import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { NgIf } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DriverActivationService } from '../service/driver-activation-service';
import { ActivateDriverRequestDTO } from '../model/ui/activate-driver-request-dto';


@Component({
  selector: 'app-driver-pass-activation',
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './driver-pass-activation.html',
  styleUrl: './driver-pass-activation.css',
})
export class DriverPassActivation {
  token: string | null = null;

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private activationService: DriverActivationService,
    private snackbar: MatSnackBar
  ) {
    this.token = this.route.snapshot.queryParamMap.get('token');
  }

  ngOnInit(){
    this.form = this.fb.group({
      newPassInput: ['', [Validators.required]],
      confirmNewPassInput: ['', [Validators.required]],
    });
  }

  submit() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const { newPassInput, confirmNewPassInput } = this.form.getRawValue();

    if (!this.token) {
      this.snackbar.open('Activation token is missing.', 'Close', { duration: 4000 });
      return;
    }

    if (newPassInput !== confirmNewPassInput) {
      this.snackbar.open('Passwords do not match.', 'Close', { duration: 4000 });
      return;
    }

    const dto:ActivateDriverRequestDTO = {
      token: this.token,
      password: newPassInput
    }

    this.activationService.activateDriver(dto).subscribe({
      next: () => {
        this.snackbar.open('Password set successfully. You can now log in.', 'Close', { duration: 4000 });
        this.router.navigateByUrl('/HomePage');
      },
      error: (err) => {
        this.snackbar.open('Activation link is invalid, expired, or already used.', 'Close', { duration: 4000 });
      }
    });
  }
}
