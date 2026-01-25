import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShowRideInconsistencies } from "../show-ride-inconsistencies/show-ride-inconsistencies";

@Component({
  selector: 'app-ride-details-admin',
  standalone: true,
  imports: [CommonModule, ShowRideInconsistencies],
  templateUrl: './ride-details-admin.html',
  styleUrl: './ride-details-admin.css',
})
export class RideDetailsAdmin {
  selectedPassenger: any = null;
  showInconsistencies: boolean = false;
  @Input() passengers: any[] = [];
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }

  showReport(passenger: any): void {
    this.selectedPassenger = passenger;
    this.showInconsistencies = true;
  }
  
  closeReport(): void {
    this.showInconsistencies = false;
    this.selectedPassenger = null;
  }

}