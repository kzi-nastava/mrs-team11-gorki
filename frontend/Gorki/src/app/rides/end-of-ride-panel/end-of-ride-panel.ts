import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DriverScheduledRide } from '../models/ride';
import { FinishRideDto } from '../../model/ui/finish-ride-dto'; 
import { EndRideService } from '../../service/end-ride-service';
@Component({
  selector: 'app-end-of-ride-panel',
  imports: [FormsModule,CommonModule],
  templateUrl: './end-of-ride-panel.html',
  styleUrl: './end-of-ride-panel.css',
})
export class EndOfRidePanel {

  isModalOpen = false;

  constructor(
    private router: Router,
    private rideService: EndRideService
  ) {}

  newRides: DriverScheduledRide[] = [];
  loadNextRide(driverId: number) {
    this.rideService.getNextScheduledRide(driverId).subscribe({
      next: ride => {
        this.newRides = [ride]; // ubaci u modal
        this.openModal();
      },
      error: err => console.error(err)
    });
  }

  endRide() {
    const driverId = 1;
    const rideId = 6; 
    const payload: FinishRideDto = {
      rideId: rideId,
      paid: true
    };

    this.rideService.finishRide(driverId, payload).subscribe({
      next: (res) => {
        console.log('Ride finished', res);

        if (res.hasNextScheduledRide) {
          this.loadNextRide(driverId);
        } else {
          this.newRides = [];
          this.openModal();
        }
      },
      error: err => console.error(err)
    });
  } 

  showMoreInfo(){

  }
  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
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
