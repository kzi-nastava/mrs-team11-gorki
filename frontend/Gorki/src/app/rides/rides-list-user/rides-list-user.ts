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
    private ratingService: RatingService) {}

  filteredRides:UserHistoryRide[]=[];
  allRides:UserHistoryRide[]=[];
  selectedRide: UserHistoryRide | null = null;
  rides:UserHistoryRide[] = [  /* // Uncomment this block when database is ready ...
    {
    id: 1,
    rating: 0,
    startTime: '17:05',
    endTime: '17:20',
    startLocation: 'Miše Dimitrijevića 5, Grbavica',
    destination: 'Jerneja Kopitara 32, Telep',
    price: 15,
    date: new Date(2026, 1, 25),
    canceled: false,
    panic: false,
    canceledBy : 'None',
    cancellationReason: 'None',
    passengers: [
      { email: 'ivan@example.com', firstName: 'Ivan', lastName: 'Ivić', phoneNumber: '0601234567' },
      { email: 'ana@example.com', firstName: 'Ana', lastName: 'Anić', phoneNumber: '0612345678' }
    ]
  },
  {
    id: 2,
    rating: 3.0,
    startTime: '14:00',
    endTime: '14:10',
    startLocation: 'Miše Dimitrijevića 5, Grbavica',
    destination: 'Hajduk Veljkova 5, Detelinara',
    price: 10,
    date: new Date(2025, 11, 16),
    canceled: false,
    panic: false,
    canceledBy : 'None',
    cancellationReason: 'None',
    passengers: [
      { email: 'ana@example.com', firstName: 'Ana', lastName: 'Anić', phoneNumber: '0612345678' },
      { email: 'ivan@example.com', firstName: 'Ivan', lastName: 'Ivić', phoneNumber: '0601234567' }
    ]
  },
  {
    id: 3,
    rating: 2.0,
    startTime: '11:00',
    endTime: '11:08',
    startLocation: 'Bulevar Oslobođenja 189, Liman 2',
    destination: 'Tolstojeva 34, Grbavica',
    price: 7,
    date: new Date(2025, 11, 9),
    canceled: true,
    panic: false,
    canceledBy : 'None',
    cancellationReason: 'None',
    passengers: [
      { email: 'marko@example.com', firstName: 'Marko', lastName: 'Marković', phoneNumber: '0623456789' },
      { email: 'jovana@example.com', firstName: 'Jovana', lastName: 'Jovanović', phoneNumber: '0634567890' },
      { email: 'ivan@example.com', firstName: 'Ivan', lastName: 'Ivić', phoneNumber: '0601234567' }
    ]
  },
  {
    id: 4,
    rating: 4.7,
    startTime: '12:00',
    endTime: '12:30',
    startLocation: 'Bulevar Oslobođenja 189, Liman 2',
    destination: 'Iriski put, Sremska Kamenica',
    price: 25,
    date: new Date(2025, 10, 11),
    canceled: false,
    panic: false,
    canceledBy : 'Jovana Jovanović',
    cancellationReason: 'reason4',
    passengers: [
      { email: 'jovana@example.com', firstName: 'Jovana', lastName: 'Jovanović', phoneNumber: '0634567890' },
      { email: 'marko@example.com', firstName: 'Marko', lastName: 'Marković', phoneNumber: '0623456789' },
      { email: 'ivan@example.com', firstName: 'Ivan', lastName: 'Ivić', phoneNumber: '0601234567' }
    ]
  },
  {
    id: 5,
    rating: 1.0,
    startTime: '19:00',
    endTime: '19:15',
    startLocation: 'Braće Dronjak 22, Bistrica',
    destination: 'Narodnog fronta 5, Liman 2',
    price: 20,
    date: new Date(2025, 10, 11),
    canceled: false,
    panic: true,
    canceledBy : 'Petar Petrović',
    cancellationReason: 'reason5',
    passengers: [
      { email: 'petar@example.com', firstName: 'Petar', lastName: 'Petrović', phoneNumber: '0645678901' },
      { email: 'jovana@example.com', firstName: 'Jovana', lastName: 'Jovanović', phoneNumber: '0634567890' },
    ]
  }  */ // ... End of comment block
  ];

  ngOnInit() {
    this.loadRides();
  }

  loadRides() {
    const userId = 2;
    this.passengerHistoryService.getUserRides(userId).subscribe({
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
      next: (res) => {
        this.selectedRideForRating!.rating =
          (event.driverRating + event.vehicleRating) / 2;

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