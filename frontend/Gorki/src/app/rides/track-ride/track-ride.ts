import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MapService } from '../../map/map-service';
import { InconsistencyService } from '../../service/inconsistency-report-service';
import { PanicService } from '../../service/panic-service';
import { AuthService } from '../../infrastructure/auth.service';
import { RideInProgressService } from '../../service/passenger-ride-in-progress';

@Component({
  selector: 'app-track-ride',
  imports: [FormsModule,CommonModule],
  templateUrl: './track-ride.html',
  styleUrl: './track-ride.css',
})
export class TrackRide {

  currentRideId: number = 0;
  showReportModal = false;
  noteText = '';

  constructor(
    private mapService: MapService,
    private inconsistencyService: InconsistencyService,
    private panicService: PanicService,
    private authService:AuthService,
    private rideInProgressService:RideInProgressService
  ) {}

  ngOnInit(){
     this.rideInProgressService
    .getActiveRide(this.authService.getId())
    .subscribe({
      next: (ride) => {
        this.currentRideId = ride.rideId; 
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  reportInconsistency() {
    this.showReportModal = true;
  }

  closeModal() {
    this.showReportModal = false;
    this.noteText = '';
  }

  submitReport() {
    if (!this.noteText.trim()) {
        alert('Unesi razlog reporta!');
        return;
    }

    this.inconsistencyService.createReport(this.currentRideId, { description: this.noteText })
      .subscribe({
        next: (response) => {
          console.log('Report created:', response);
          alert('Report sent successfully!');
          this.closeModal();
        },
        error: (err) => {
          console.error('Error creating report:', err);
          alert('Some error occured while sending report.');
        }
    });
  }

  panic() {
    this.panicService.triggerPanic(this.currentRideId).subscribe({
      next: () => {
        this.mapService.setDriverToPanic();
        alert('PANIC recorded!');
      },
      error: (err) => {
        alert(err?.error?.message ?? err?.error ?? 'PANIC failed');
      }
    });
  }

}
