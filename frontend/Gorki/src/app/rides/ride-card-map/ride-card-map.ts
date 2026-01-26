import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ride, UserHistoryRide } from '../models/ride';

@Component({
  selector: 'app-ride-card-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ride-card-map.html',
  styleUrl: './ride-card-map.css'
})
export class RideCardMap {
  @Input() ride!: UserHistoryRide;
  @Output() openRideDetails = new EventEmitter<UserHistoryRide>();

  showDetails() {
    this.openRideDetails.emit(this.ride);
  }
}