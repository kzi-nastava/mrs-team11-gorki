import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ride } from '../models/ride';

@Component({
  selector: 'app-ride-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ride-card.html',
  styleUrl: './ride-card.css'
})
export class RideCard {
  @Input() ride!: Ride;
  @Output() moreInfo = new EventEmitter<Ride>();

  openDetails() {
    this.moreInfo.emit(this.ride);
  }
}