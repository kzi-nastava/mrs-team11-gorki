import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RideDetails } from "../ride-details/ride-details";
import { Passenger, ScheduledRide } from '../models/ride';
import { RideCardScheduled } from "../ride-card-scheduled/ride-card-scheduled";
import { CancellationReasonForm } from "../cancellation-reason-form/cancellation-reason-form";

@Component({
  selector: 'app-scheduled-rides',
  standalone: true,
  imports: [CommonModule, RideCardScheduled, RideDetails, CancellationReasonForm],
  templateUrl: './scheduled-rides.html',
  styleUrl: './scheduled-rides.css',
})

export class ScheduledRides {
  selectedRideDetails: ScheduledRide | null = null;
  selectedRideCancellation: ScheduledRide | null = null;
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

  rides:ScheduledRide[] = [
    {
    id: 1,
    rating: 0,
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
    rating: 0,
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
    rating: 0,
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
    rating: 0,
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
    rating: 0,
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

  filteredRides:ScheduledRide[]=[];
  allRides:ScheduledRide[]=[];
  ngOnInit() {
    this.filteredRides = [...this.rides];
    this.allRides=[...this.rides];
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