import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DriverScheduledRide } from '../models/ride';

@Component({
  selector: 'app-end-of-ride-panel',
  imports: [FormsModule,CommonModule],
  templateUrl: './end-of-ride-panel.html',
  styleUrl: './end-of-ride-panel.css',
})
export class EndOfRidePanel {

isModalOpen = false;

  constructor (private router:Router){};
  // privremeni "mock" niz
  // []  -> nema nove voznje
  // [{}] -> postoji nova voznja
  newRides: DriverScheduledRide[] = [
    {
      id: 1,
      rating: 0,
      startTime: '17:05',
      startLocation: 'Miše Dimitrijevića 5, Grbavica',
      destination: 'Jerneja Kopitara 32, Telep',
      price: 15,
      date: new Date(2026, 0, 16),
      canceled: false,
      cancelationReason: 'None',
      panic: false,
      passengers: [
        { email: 'ivan@example.com', firstName: 'Ivan', lastName: 'Ivić', phoneNumber: '0601234567' },
        { email: 'ana@example.com', firstName: 'Ana', lastName: 'Anić', phoneNumber: '0612345678' }
        ]
    }
  ];

  showMoreInfo(){

  }
  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  endRide() {
    this.openModal();
  }

  startRide() {
    console.log('START RIDE');
    this.closeModal();
    //ucitavanje novih podataka
  }

  goToScheduledRides() {
    console.log('GO TO SCHEDULED RIDES');
    this.router.navigateByUrl('driver-sceduled-rides');
    this.closeModal();
  }
  
}
