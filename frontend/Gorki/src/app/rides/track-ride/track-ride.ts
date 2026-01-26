import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MapService } from '../../map/map-service';

@Component({
  selector: 'app-track-ride',
  imports: [FormsModule,CommonModule],
  templateUrl: './track-ride.html',
  styleUrl: './track-ride.css',
})
export class TrackRide {

  constructor(private mapService: MapService) {}

  showReportModal = false;
  noteText = '';

  reportInconsistency() {
    this.showReportModal = true;
  }

  closeModal() {
    this.showReportModal = false;
    this.noteText = '';
  }

  submitReport() {
    console.log('Report note:', this.noteText);
    this.closeModal();
  }

  panic() {
    this.mapService.setDriverToPanic();
  }

}
