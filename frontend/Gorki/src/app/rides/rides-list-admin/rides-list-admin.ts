import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';
import { Ride,Passenger, UserHistoryRide } from '../models/ride';
import { RideCardUser } from '../ride-card-user/ride-card-user';
import { PersonFilter } from "../filters/person-filter/person-filter";
import { AdminHistoryService } from '../../service/admin-history-service';

@Component({
  selector: 'app-rides-list-admin',
  standalone: true,
  imports: [CommonModule, RideCardUser, DateFilter, SortFilter, PersonFilter],
  templateUrl: './rides-list-admin.html',
  styleUrl: './rides-list-admin.css',
})
export class RidesListAdmin {

  constructor(
    private router: Router, 
    private adminHistoryService: AdminHistoryService
  ) {}

  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;
  
  allRides:UserHistoryRide[]=[];
  filteredRides:UserHistoryRide[]=[];
  selectedRide: UserHistoryRide | null = null;
  rides:UserHistoryRide[] = [  //Uncomment this block when database is ready ...
    {
    id: 1,
    rating: 4.5,
    startTime: '17:05',
    endTime: '17:20',
    startLocation: 'Miše Dimitrijevića 5, Grbavica',
    destination: 'Jerneja Kopitara 32, Telep',
    price: 15,
    date: new Date(2025, 11, 16),
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
  }  // ... End of comment block
  ]; 

  ngOnInit() {
    this.loadRides();
  }

  loadRides() {
    const driverId = 1;
    this.adminHistoryService.getAdminRides(driverId).subscribe({
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

  this.filteredRides.sort((a, b) => {
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
    }

    return order === 'asc' ? comparison : -comparison;
    });
  }

  filterByDate(event: { from: Date | null; to: Date | null }) {
    const { from, to } = event;
    this.filteredRides = [...this.allRides]; // reset

    if (!from && !to) {
      return;
    }

    this.filteredRides = this.filteredRides.filter(ride => {
      const rideDate = new Date(ride.date);

      if (from && to) return rideDate >= from && rideDate <= to;
      if (from) return rideDate >= from;
      if (to) return rideDate <= to;

      return true;
    });
  }

  filterByPerson(event: { person: string | null }) {
    const { person } = event;

    if (!person) {
      this.filteredRides = [...this.allRides];
      return;
    }

    this.filteredRides = this.allRides.filter(ride =>
      ride.passengers.some(p =>
        `${p.firstName} ${p.lastName}`.toLowerCase() === person.toLowerCase()
      )
    );
  }

}