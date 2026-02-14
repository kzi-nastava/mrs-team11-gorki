import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { PriceConfigService, PriceConfig } from '../../service/price-config-service';

@Component({
  selector: 'app-change-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './change-form.html',
  styleUrls: ['./change-form.css'],
})
export class ChangeForm implements OnInit {
  private fb = inject(FormBuilder);
  private snack = inject(MatSnackBar);
  private priceConfigService = inject(PriceConfigService);

  editMode = false;
  saving = false;

  private initialValue: PriceConfig | null = null;

  form = this.fb.group({
    priceForStandardVehicles: this.fb.nonNullable.control(0, [
      Validators.required,
      Validators.min(0),
    ]),
    priceForLuxuryVehicles: this.fb.nonNullable.control(0, [
      Validators.required,
      Validators.min(0),
    ]),
    priceForVans: this.fb.nonNullable.control(0, [
      Validators.required,
      Validators.min(0),
    ]),
    pricePerKm: this.fb.nonNullable.control(0, [
      Validators.required,
      Validators.min(0),
    ]),
  });

  ngOnInit(): void {
    this.fetchCurrentConfig();
  }

  fetchCurrentConfig(): void {

  this.priceConfigService.getCurrentConfig().subscribe({
    next: (cfg) => {
      console.log('PRICE CONFIG OK:', cfg);
      this.form.patchValue(cfg);
      this.form.markAsPristine();
      this.initialValue = { ...this.form.getRawValue() };
      this.editMode = false;
    },
    error: (err) => {
      console.error('PRICE CONFIG ERROR:', err);
      this.snack.open('Failed to load price config', 'OK', { duration: 3000 });
    },
  });
}

  enableEdit(): void {
    this.editMode = true;
  }

  cancel(): void {
    if (this.initialValue) {
      this.form.reset(this.initialValue);
      this.form.markAsPristine();
    }
    this.editMode = false;
  }

  save(): void {
    if (this.form.invalid || this.form.pristine) return;

    this.saving = true;
    const payload: PriceConfig = this.form.getRawValue();

    this.priceConfigService.updateConfig(payload).subscribe({
      next: (updated) => {
        this.form.patchValue(updated);
        this.form.markAsPristine();
        this.initialValue = { ...this.form.getRawValue() };
        this.editMode = false;
        this.saving = false;
        this.snack
        this.snack.open('Price config updated', 'OK', { duration: 2500, panelClass: ['custom-snackbar'] });
      },
      error: (err) => {
        console.error(err);
        this.saving = false;
        this.snack.open('Update failed', 'OK', { duration: 3000, panelClass: ['custom-snackbar'] });
      },
    });
  }

  isInvalid(controlName: keyof PriceConfig): boolean {
    const c = this.form.get(controlName as string);
    return !!c && c.invalid && (c.touched || c.dirty);
  }
}
