import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';
import { Ride,Passenger, UserHistoryRide } from '../models/ride';
import { RideCardUser } from '../ride-card-user/ride-card-user';
import { RatingPanel } from "../rating-panel/rating-panel";

@Component({
  selector: 'app-rides-list-user',
  standalone: true,
  imports: [CommonModule, RideCardUser, DateFilter, SortFilter, RideDetails, RatingPanel],
  templateUrl: './rides-list-user.html',
  styleUrl: './rides-list-user.css',
})
export class RidesListUser {
  selectedRide: UserHistoryRide | null = null;
  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;
  
  constructor(private router: Router) {}

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

  rides:UserHistoryRide[] = [
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
  }
  ];

  filteredRides:UserHistoryRide[]=[];
  allRides:UserHistoryRide[]=[];
  ngOnInit() {
    this.filteredRides = [...this.rides];
    this.allRides=[...this.rides];
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
    this.rides=[...this.allRides];
    const { from, to } = event;
   
    if (!from && !to) {
      this.filteredRides = [...this.rides];
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

      // primer: prosek ili samo driver rating
      const now = Date.now();
      const threeDaysInMs = 3 * 24 * 60 * 60 * 1000;

      if (this.selectedRideForRating.date.getTime() > now - threeDaysInMs) {
        // ovde kasnije ide backend call
        // this.rideService.rateRide(...)
          this.selectedRideForRating.rating =
          (event.driverRating + event.vehicleRating) / 2;
          this.showRating = false;
          this.selectedRideForRating = null;
      }
    }
}