import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rating-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rating-panel.html',
  styleUrl: './rating-panel.css',
})
export class RatingPanel {

  driverRating: number = 0;
  vehicleRating: number = 0;
  comment: string = '';

  @Output() submit = new EventEmitter<{
    driverRating: number;
    vehicleRating: number;
    comment: string;
  }>();

  @Output() close = new EventEmitter<void>();

  setDriverRating(value: number) {
    this.driverRating = value;
  }

  setVehicleRating(value: number) {
    this.vehicleRating = value;
  }

  onSubmit() {
    this.submit.emit({
      driverRating: this.driverRating,
      vehicleRating: this.vehicleRating,
      comment: this.comment,
    });

    this.close.emit();
  }
}