import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RideCard } from '../ride-card/ride-card';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';
import { RideDetails } from "../ride-details/ride-details";
import { DriverHistoryService } from '../../service/driver-history-service';
import { Ride } from '../models/ride';

@Component({
  selector: 'app-rides-list',
  standalone: true,
  imports: [CommonModule, RideCard, DateFilter, SortFilter, RideDetails],
  templateUrl: './rides-list.html',
  styleUrl: './rides-list.css',
})
export class RidesList {
  rides: Ride[] = [];
  allRides: Ride[] = [];
  selectedRide: Ride | null = null;

  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;

  constructor(private rideHistoryService: DriverHistoryService) {}

  ngOnInit() {
    this.loadRides();
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

  loadRides() {
    const driverId = 1;
    this.rideHistoryService.getDriverRides(driverId).subscribe({
      next: (rides) => {
        this.rides = [...rides];
        this.allRides = [...rides];
      },
      error: (err) => {
        console.error('Ne mogu da dohvatim voznje', err);
      },
    });
  }
  
  filteredRides:Ride[]=[];

  openRideDetails(ride: Ride) {
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