import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-driver-scheduled-ride-details',
  imports: [CommonModule],
  templateUrl: './driver-scheduled-ride-details.html',
  styleUrl: './driver-scheduled-ride-details.css',
})
export class DriverScheduledRideDetails {
  @Input() passengers: any[] = [];
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}
