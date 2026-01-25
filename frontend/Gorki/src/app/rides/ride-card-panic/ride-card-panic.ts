import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanicRide, Ride } from '../models/ride';

@Component({
  selector: 'app-ride-card-panic',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ride-card-panic.html',
  styleUrl: './ride-card-panic.css'
})
export class RideCardPanic {
  @Input() ride!: PanicRide;
  @Output() moreInfo = new EventEmitter<PanicRide>();

  openDetails() {
    this.moreInfo.emit(this.ride);
  }
}