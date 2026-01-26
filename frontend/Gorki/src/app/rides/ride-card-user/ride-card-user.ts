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
  @Output() goToMap = new EventEmitter<number>();
  //@Output() moreInfo = new EventEmitter<UserHistoryRide>();
  @Output() rateRide = new EventEmitter<UserHistoryRide>();

  goToMapClick() {
    this.goToMap.emit(this.ride.id);
  }

  openRatingModal(){
    this.rateRide.emit(this.ride);
  }

  canRate(): boolean {
      const now = new Date();
      const threeDaysAgo = new Date();
      threeDaysAgo.setDate(now.getDate() - 3);

      return this.ride.date > threeDaysAgo && !this.ride.canceled && this.ride.rating==0;
  }
}