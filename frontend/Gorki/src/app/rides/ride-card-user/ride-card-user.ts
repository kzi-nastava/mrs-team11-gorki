import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ride, UserHistoryRide } from '../models/ride';

@Component({
  selector: 'app-ride-card-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ride-card-user.html',
  styleUrl: './ride-card-user.css'
})
export class RideCardUser {
  @Input() ride!: UserHistoryRide;
  @Output() moreInfo = new EventEmitter<UserHistoryRide>();

  openDetails() {
    this.moreInfo.emit(this.ride);
  }
}