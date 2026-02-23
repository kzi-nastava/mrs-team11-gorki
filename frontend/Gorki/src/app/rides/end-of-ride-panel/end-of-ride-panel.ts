import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DriverScheduledRide, Ride } from '../models/ride';
import { FinishRideDto } from '../../model/ui/finish-ride-dto'; 
import { EndRideService } from '../../service/end-ride-service';
import { AuthService } from '../../infrastructure/auth.service';
import { DriverRideInProgress} from '../../service/driver-ride-in-progress';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-end-of-ride-panel',
  imports: [FormsModule,CommonModule],
  templateUrl: './end-of-ride-panel.html',
  styleUrl: './end-of-ride-panel.css',
})
export class EndOfRidePanel {

  isModalOpen = false;
  driverId:number=0;
  rideId:number=0;

  ngOnInit(){
    this.driverId = this.authService.getId();
    this.driverService
          .getActiveRide(this.authService.getId())
          .subscribe({
            next:(ride)=>{
              this.rideId=ride.rideId;
            },
            error:(err)=>{
              console.log(err);
            }
    });
  }

  constructor(
    private router: Router,
    private rideService: EndRideService,
    private authService:AuthService,
    private driverService:DriverRideInProgress,
    private cdr:ChangeDetectorRef
  ) {}

  newRides: DriverScheduledRide[] = [];
  loadNextRide(driverId: number) {
    this.rideService.getNextScheduledRide(driverId).subscribe({
      next: ride => {
        this.newRides = [ride]; 
        this.openModal();
      },
      error: err => console.error(err)
    });
  }

  endRide() {
    const payload: FinishRideDto = {
      rideId: this.rideId,
      paid: true
    };

    this.rideService.finishRide(this.driverId, payload).subscribe({
      next: (res) => {
        if (res.hasNextScheduledRide) {
          this.loadNextRide(this.driverId);
        } else {
          this.newRides = [];
          this.openModal();
        }
      },
      error: err => console.error(err)
    });
  } 

  showMoreInfo(){
    //dovrsiti
  }
  openModal() {
    this.isModalOpen = true;
    this.cdr.detectChanges();
  }

  closeModal() {
    this.isModalOpen = false;
  }

  startRide() {
    console.log('START RIDE');
    this.closeModal();
    this.router.navigateByUrl('HomePage');

  }

  goToScheduledRides() {
    console.log('GO TO SCHEDULED RIDES');
    this.router.navigateByUrl('scheduled-rides');
    this.closeModal();
  }
  
}
