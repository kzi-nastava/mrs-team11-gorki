import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';
import { Ride,Passenger, UserHistoryRide } from '../models/ride';
import { RideCardUser } from '../ride-card-user/ride-card-user';
import { RatingPanel } from "../rating-panel/rating-panel";
import { RatingService } from '../../service/rating-service';
import { PassengerHistoryService } from '../../service/passenger-history-service';
import { AuthService } from '../../infrastructure/auth.service';

@Component({
  selector: 'app-rides-list-user',
  standalone: true,
  imports: [CommonModule, RideCardUser, DateFilter, SortFilter, RatingPanel],
  templateUrl: './rides-list-user.html',
  styleUrl: './rides-list-user.css',
})
export class RidesListUser {

  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;
  
  constructor(
    private passengerHistoryService: PassengerHistoryService,
    private router: Router, 
    private ratingService: RatingService,
    private authService:AuthService) {}

  filteredRides:UserHistoryRide[]=[];
  allRides:UserHistoryRide[]=[];
  selectedRide: UserHistoryRide | null = null;
  rides:UserHistoryRide[] = [];

  ngOnInit() {
    this.loadRides();
  }

  loadRides() {
    const userId = this.authService.getId();
    this.passengerHistoryService.getUserRides(userId).subscribe({
      next: (rides) => {
        this.rides = [...rides];
        this.allRides = [...rides];
        console.log(this.rides);
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
  
  openMap(ride: UserHistoryRide) {
  this.router.navigate(['/ride-list-map', ride.id], { state: { ride } });
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

  filterByDate(event: { from: Date | null; to: Date | null }) {
    this.filteredRides=[...this.allRides];
    const { from, to } = event;
   
    if (!from && !to) {
      this.filteredRides = [...this.filteredRides];
      return;
    }

    this.rides = this.rides.filter(ride => {
      const rideDate = new Date(ride.date);

      if (from && to) {
        return rideDate >= from && rideDate <= to;
      }

      if (from) {
        return rideDate >= from;
      }

      if (to) {
        return rideDate <= to;
      }

      return true;
    });
  }

  selectedRideForRating: UserHistoryRide | null = null;
  showRating = false;
  openRatingPanel(ride: UserHistoryRide) {
    this.selectedRideForRating = ride;
    this.showRating = true;
  } 

  handleRating(event: {
    driverRating: number;
    vehicleRating: number;
    comment: string;
  }) {
    if (!this.selectedRideForRating) return;

    const rideId = this.selectedRideForRating.id;

    this.ratingService.rateRide(rideId, {
      driverRating: event.driverRating,
      vehicleRating: event.vehicleRating,
      comment: event.comment
    }).subscribe({
      next: () => {

        const newRating =
          (event.driverRating + event.vehicleRating) / 2;

        this.rides = this.rides.map(r =>
          r.id === rideId ? { ...r, rating: newRating } : r
        );

        this.showRating = false;
        this.selectedRideForRating = null;
      },
      error: (err) => {
        console.error("Greska pri ocenjivanju", err);
        alert("Rating nije uspeo");
      }
    });
}
}