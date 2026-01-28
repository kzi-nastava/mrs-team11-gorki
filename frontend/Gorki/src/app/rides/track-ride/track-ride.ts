import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MapService } from '../../map/map-service';
import { InconsistencyService } from '../../service/inconsistency-report-service';

@Component({
  selector: 'app-track-ride',
  imports: [FormsModule,CommonModule],
  templateUrl: './track-ride.html',
  styleUrl: './track-ride.css',
})
export class TrackRide {

  currentRideId: number = 6;
  showReportModal = false;
  noteText = '';

  constructor(
    private mapService: MapService,
    private inconsistencyService: InconsistencyService
  ) {}



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
    this.mapService.setDriverToPanic();
  }

}
