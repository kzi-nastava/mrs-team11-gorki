import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DriverScheduledRide } from '../models/ride';

@Component({
  selector: 'app-driver-scheduled-ride-card',
  imports: [CommonModule],
  templateUrl: './driver-scheduled-ride-card.html',
  styleUrl: './driver-scheduled-ride-card.css',
})
export class DriverScheduledRideCard {
  @Input() scheduledRide!: DriverScheduledRide;
  
  @Output() moreInfo = new EventEmitter<DriverScheduledRide>();
  @Output() cacneledRide = new EventEmitter<DriverScheduledRide>();

  openDetails() {
    this.moreInfo.emit(this.scheduledRide);
  }

  cancelRide(){
    this.cacneledRide.emit(this.scheduledRide);

  }
}
