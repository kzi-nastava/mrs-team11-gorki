import { Component, OnDestroy, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Subscription, timer, of } from 'rxjs';
import { catchError, take } from 'rxjs/operators';

import { AdminRideMonitorService } from '../../service/admin-ride-monitor-service';
import { AdminRideMonitorDTO, GetDriverInfoDTO } from '../../model/ui/admin-ride-monitor-dto';

import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-admin-ride-monitor',
  templateUrl: './admin-ride-monitor.html',
  styleUrls: ['./admin-ride-monitor.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatDividerModule,
    MatChipsModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatListModule,
    MatSlideToggleModule,
    MatTableModule
  ]
})
export class AdminRideMonitor implements OnInit, OnDestroy {

  searchCtrl = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required, Validators.minLength(2)]
  });

  foundDriver: GetDriverInfoDTO | null = null;
  activeRide: AdminRideMonitorDTO | null = null;

  isSearching = false;
  isLoadingRide = false;

  searched = false;

  pollingEnabled = true;
  pollingSeconds = 5;
  private pollingSub?: Subscription;

  private findSub?: Subscription;
  private loadRideSub?: Subscription;

  constructor(
    private adminService: AdminRideMonitorService,
    private snack: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {
    this.stopPolling();
    this.findSub?.unsubscribe();
    this.loadRideSub?.unsubscribe();
  }

  clearAll(): void {
    this.findSub?.unsubscribe();
    this.loadRideSub?.unsubscribe();
    this.stopPolling();

    this.searchCtrl.setValue('');
    this.foundDriver = null;
    this.activeRide = null;
    this.searched = false;

    this.isSearching = false;
    this.isLoadingRide = false;

    this.cdr.detectChanges();
  }

  private forceUi(): void {
    this.zone.run(() => {
      this.cdr.detectChanges();
    });
  }

  findDriver(): void {
    const term = this.searchCtrl.value.trim();

    if (!term || term.length < 2) {
      this.searchCtrl.markAsTouched();
      return;
    }
    if (this.isSearching) return;

    this.findSub?.unsubscribe();

    this.searched = true;
    this.foundDriver = null;
    this.activeRide = null;
    this.stopPolling();

    this.isSearching = true;
    this.forceUi();

    this.findSub = this.adminService.searchDriver(term).pipe(
      take(1),
      catchError(err => {
        this.isSearching = false;

        if (err?.status === 404) {
          this.foundDriver = null;
          setTimeout(() => this.forceUi(), 0);
          return of(null);
        }

        this.snack.open('Driver does not exist', 'OK', { duration: 3000,panelClass: ['custom-snackbar'] });
        setTimeout(() => this.forceUi(), 0);
        return of(null);
      })
    ).subscribe({
      next: (driver) => {
        this.isSearching = false;
        this.foundDriver = driver;
        setTimeout(() => this.forceUi(), 0);
      },
      error: () => {
        this.isSearching = false;
        setTimeout(() => this.forceUi(), 0);
      },
      complete: () => {
        this.isSearching = false;
        setTimeout(() => this.forceUi(), 0);
      }
    });
  }

  loadActiveRide(): void {
    if (!this.foundDriver) return;
    if (this.isLoadingRide) return;

    const driverId = this.foundDriver.user.id;

    this.loadRideSub?.unsubscribe();

    this.isLoadingRide = true;
    this.forceUi();

    this.loadRideSub = this.adminService.getActiveRide(driverId).pipe(
      take(1),
      catchError(err => {
        this.isLoadingRide = false;

        if (err?.status === 404) {
          this.activeRide = null;
          this.snack.open('Driver does not have active ride.', 'OK', { duration: 3000,panelClass: ['custom-snackbar'] });
          setTimeout(() => this.forceUi(), 0);
          return of(null);
        }

        this.snack.open('Driver does not have active ride.', 'OK', { duration: 3000,panelClass: ['custom-snackbar'] });
        setTimeout(() => this.forceUi(), 0);
        return of(null);
      })
    ).subscribe({
      next: (dto) => {
        this.isLoadingRide = false;
        if (!dto) {
          setTimeout(() => this.forceUi(), 0);
          return;
        }

        this.activeRide = dto;

        if (this.pollingEnabled) {
          this.startPolling(driverId);
        }

        setTimeout(() => this.forceUi(), 0);
      },
      error: () => {
        this.isLoadingRide = false;
        setTimeout(() => this.forceUi(), 0);
      },
      complete: () => {
        this.isLoadingRide = false;
        setTimeout(() => this.forceUi(), 0);
      }
    });
  }

  private startPolling(driverId: number): void {
    this.stopPolling();

    this.pollingSub = timer(this.pollingSeconds * 1000, this.pollingSeconds * 1000)
      .subscribe(() => {
        this.adminService.getActiveRide(driverId).pipe(take(1)).subscribe({
          next: (dto) => {
            if (!dto) {
              this.activeRide = null;
              this.stopPolling();
              this.forceUi();
              return;
            }
            this.activeRide = dto;
            this.forceUi();
          },
          error: () => {
            this.activeRide = null;
            this.stopPolling();
            this.forceUi();
          }
        });
      });
  }

  private stopPolling(): void {
    this.pollingSub?.unsubscribe();
    this.pollingSub = undefined;
  }

  togglePolling(enabled: boolean): void {
    this.pollingEnabled = enabled;

    if (!enabled) {
      this.stopPolling();
      return;
    }

    if (this.foundDriver && this.activeRide) {
      this.startPolling(this.foundDriver.user.id);
    }
  }

  fullName(driver: GetDriverInfoDTO): string {
    return `${driver.user.firstName} ${driver.user.lastName}`;
  }
}
