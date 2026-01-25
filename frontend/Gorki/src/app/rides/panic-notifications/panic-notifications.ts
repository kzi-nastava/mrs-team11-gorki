import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RideDetailsAdmin } from "../ride-details-admin/ride-details-admin";
import { Ride,Passenger, PanicRide } from '../models/ride';
import { RideCardPanic } from '../ride-card-panic/ride-card-panic';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';

@Component({
  selector: 'app-panic-notifications',
  standalone: true,
  imports: [CommonModule, RideCardPanic, RideDetailsAdmin, DateFilter, SortFilter],
  templateUrl: './panic-notifications.html',
  styleUrl: './panic-notifications.css',
})

export class PanicNotifications {
  selectedRide: PanicRide | null = null;
  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;

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

  rides:PanicRide[] = [
    {
    id: 1,
    rating: 4.5,
    startTime: '17:05',
    endTime: '17:20',
    startLocation: 'Miše Dimitrijevića 5, Grbavica',
    destination: 'Jerneja Kopitara 32, Telep',
    price: 15,
    date: new Date(2025, 11, 16),
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
    destination: 'Bul. Mihaila Pupina 68, Centar',
    price: 10,
    date: new Date(2025, 11, 16),
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
    destination: 'Tolstojeva 34, Centar',
    price: 7,
    date: new Date(2025, 11, 9),
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
    startLocation: 'Futoški put 29, Bistrica',
    destination: 'Sremska 9, Stari grad',
    price: 20,
    date: new Date(2025, 10, 11),
    passengers: [
      { email: 'petar@example.com', firstName: 'Petar', lastName: 'Petrović', phoneNumber: '0645678901' }
    ]
  }
  ];

  filteredRides:PanicRide[]=[];
  allRides:PanicRide[]=[];
  ngOnInit() {
    this.filteredRides = [...this.rides];
    this.allRides=[...this.rides];
  }

  openRideDetails(ride: PanicRide) {
    this.selectedRide = ride;
  }

  closeRideDetails() {
    this.selectedRide = null;
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

}