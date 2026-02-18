import { Component, ViewChild, ElementRef, Output,EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RideDetails } from "../ride-details/ride-details";
import { Passenger, ScheduledRide } from '../models/ride';
import { RideCardScheduled } from "../ride-card-scheduled/ride-card-scheduled";
import { CancellationReasonForm } from "../cancellation-reason-form/cancellation-reason-form";
import { CancelRideService } from '../../service/cancel-ride-service';
import { AuthService } from '../../infrastructure/auth.service';
import { ScheduledService } from '../../service/scheduled-service';

@Component({
  selector: 'app-scheduled-rides',
  standalone: true,
  imports: [CommonModule, RideCardScheduled, RideDetails, CancellationReasonForm],
  templateUrl: './scheduled-rides.html',
  styleUrl: './scheduled-rides.css',
})

export class ScheduledRides {

  constructor(
    private cancelRideService: CancelRideService,
    private authService: AuthService,
    private scheduledService: ScheduledService
  ) {}

  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;

  selectedRideDetails: ScheduledRide | null = null;
  selectedRideCancellation: ScheduledRide | null = null;
  allRides:ScheduledRide[]=[];
  filteredRides:ScheduledRide[]=[];
  rides:ScheduledRide[] = [];

  ngOnInit() {
    this.loadRides();
  }

  loadRides() {
    const userId = this.authService.getId(); 
    this.scheduledService.getScheduledRides(userId).subscribe({
      next: (rides) => {
        this.rides = [...rides];
        this.allRides = [...rides];
      },
      error: (err) => {
        console.error('Ne mogu da dohvatim voznje', err);
      },
    });
  }

  scrollLeft() {
    this.carousel.nativeElement.scrollBy({
      left: -300,
      behavior: 'smooth',
    });
  }

  scrollRight() {
    this.carousel.nativeElement.scrollBy({
      left: 300,
      behavior: 'smooth',
    });
  }
  
  openRideDetails(ride: ScheduledRide) {
    this.selectedRideDetails = ride;
  }

  closeRideDetails() {
    this.selectedRideDetails = null;
  }

  openCancellationForm(ride: ScheduledRide) {
    this.selectedRideCancellation = ride;
  }
  
  closeCancellationForm() {
    this.selectedRideCancellation = null;
  }

  confirmCancellation(reason: string) {
    if (!this.selectedRideCancellation) return;

    const rideId = this.selectedRideCancellation.id;

    const actorId = this.authService.getId();
    const cancelledBy: 'PASSENGER' | 'DRIVER' = 'PASSENGER';

    this.cancelRideService.cancelRide(rideId, {
      cancellationReason: reason.trim(),
      cancelledBy,
      actorId
    }).subscribe({
      next: (res) => {
        this.rides = this.rides.map(r =>
          r.id === res.rideId
            ? { ...r, canceled: true, cancelationReason: res.cancellationReason }
            : r
        );

        this.filteredRides = this.filteredRides.map(r =>
          r.id === res.rideId
            ? { ...r, canceled: true, cancelationReason: res.cancellationReason }
            : r
        );

        this.selectedRideCancellation = null;
      },
      error: (err) => {
        alert(err?.error?.message ?? err?.error ?? 'Cancel failed');
      }
    });
  }

  sortRides(event: {criteria: string, order: 'asc' | 'desc'}) {
    const { criteria, order } = event;

    this.rides.sort((a, b) => {
      let comparison = 0;

      switch(criteria) {
        case 'startTime':
        case 'endTime':
        case 'startLocation':
        case 'destination':
          comparison = a[criteria].localeCompare(b[criteria]);
          break;
        case 'price':
          comparison = a.price - b.price;
          break;
        default:
          comparison = 0;
      }

      return order === 'asc' ? comparison : -comparison;
    });
  }

}