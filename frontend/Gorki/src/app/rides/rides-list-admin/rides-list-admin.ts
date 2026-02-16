import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';
import { Ride,Passenger, UserHistoryRide } from '../models/ride';
import { RideCardUser } from '../ride-card-user/ride-card-user';
import { PersonFilter } from "../filters/person-filter/person-filter";
import { AdminHistoryService } from '../../service/admin-history-service';
import { AuthService } from '../../infrastructure/auth.service';

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
    private adminHistoryService: AdminHistoryService,
    private authService: AuthService
  ) {}

  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;
  
  allRides:UserHistoryRide[]=[];
  filteredRides:UserHistoryRide[]=[];
  selectedRide: UserHistoryRide | null = null;
  rides:UserHistoryRide[] = []; 

  ngOnInit() {
    this.loadRides();
  }

  loadRides() {
    this.adminHistoryService.getPanicRides().subscribe({
      next: (rides) => {
        this.rides = [...rides];
        this.allRides = [...rides];
      },
      error: (err) => console.error('Ne mogu da dohvatim panic voznje', err),
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
      this.filteredRides = [...this.allRides]; //clear
      return;
    }

    this.filteredRides = this.allRides.filter(ride =>
      ride.passengers.some(p =>
        `${p.firstName} ${p.lastName}`.toLowerCase() === person.toLowerCase()
      )
    );
  }

}