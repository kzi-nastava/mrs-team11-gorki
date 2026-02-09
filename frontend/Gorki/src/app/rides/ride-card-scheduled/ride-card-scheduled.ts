import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ScheduledRide } from '../models/ride';

@Component({
  selector: 'app-ride-card-scheduled',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ride-card-scheduled.html',
  styleUrl: './ride-card-scheduled.css'
})
export class RideCardScheduled {
  @Input() ride!: ScheduledRide;
  @Output() moreInfo = new EventEmitter<ScheduledRide>();
  @Output() cancellationReasonForm = new EventEmitter<ScheduledRide>();

  openDetails() {
    this.moreInfo.emit(this.ride);
  }

  openCancellationReasonForm() {
    this.cancellationReasonForm.emit(this.ride);
  }

}