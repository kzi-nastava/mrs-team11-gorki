import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-track-ride',
  imports: [FormsModule,CommonModule],
  templateUrl: './track-ride.html',
  styleUrl: './track-ride.css',
})
export class TrackRide {

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
}
