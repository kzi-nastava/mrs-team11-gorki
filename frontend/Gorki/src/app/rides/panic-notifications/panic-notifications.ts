import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RideDetailsAdmin } from "../ride-details-admin/ride-details-admin";
import { Ride,Passenger, PanicRide } from '../models/ride';
import { RideCardPanic } from '../ride-card-panic/ride-card-panic';
import { DateFilter } from '../filters/date-filter/date-filter';
import { SortFilter } from '../filters/sort-filter/sort-filter';
import { AuthService } from '../../infrastructure/auth.service';
import { AdminHistoryService } from '../../service/admin-history-service';
import { PanicSocketService } from '../../service/panic-socket-service';

@Component({
  selector: 'app-panic-notifications',
  standalone: true,
  imports: [CommonModule, RideCardPanic, RideDetailsAdmin, DateFilter, SortFilter],
  templateUrl: './panic-notifications.html',
  styleUrl: './panic-notifications.css',
})

export class PanicNotifications {

  constructor(
    private authService: AuthService,
    private adminHistoryService: AdminHistoryService,
    private panicSocketService: PanicSocketService
  ) {}

  selectedRide: PanicRide | null = null;
  filteredRides:PanicRide[]=[];
  allRides:PanicRide[]=[];
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

  rides:PanicRide[] = [];

  ngOnInit() {
    this.loadRides();

    const token = localStorage.getItem('user');
    if (token) {
      this.panicSocketService.connect(token, (evt) => {
        this.loadRides();
        this.playSound();
        alert('New panic notification received!');
      });
    }
  }

  private audio = new Audio('notification-sound-1.wav');

  playSound() {
    this.audio.currentTime = 0;
    this.audio.play().catch(() => {
      // browser blokira autoplay dok user ne klikne negde
    });
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
      this.rides = [...this.rides];
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