import { Component, OnInit } from '@angular/core';
import { MapComponent } from "../map/map";
import { MapService } from '../map/map-service';
import { TrackRideDriver } from '../rides/track-ride-driver/track-ride-driver';
import { DriverRideInProgress } from '../service/driver-ride-in-progress';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../infrastructure/auth.service';

@Component({
  selector: 'app-ride-in-progress-driver',
  standalone: true,
  imports: [MapComponent, TrackRideDriver, CommonModule],
  templateUrl: './ride-in-progress-driver.html',
  styleUrls: ['./ride-in-progress-driver.css'],
})
export class RideInProgressDriver implements OnInit {

  pickupAddress: string = '';
  dropoffAddress: string = '';

  constructor(
      private mapService: MapService,
      private rideService: DriverRideInProgress,
      private cdr: ChangeDetectorRef,
      private authService: AuthService
    ) {}

  async ngOnInit() {
    this.mapService.clearAll();

    this.rideService.getActiveRideAddresses(this.authService.getId()).subscribe({
      next: async ({ pickup, dropoff }) => {
        this.pickupAddress = pickup;
        this.dropoffAddress = dropoff;

        this.pickupAddress = pickup;
        this.dropoffAddress = dropoff;

        this.cdr.detectChanges();

        const pickupCoords = await this.geocode(pickup);
        const dropoffCoords = await this.geocode(dropoff);

        await this.mapService.showSnappedRoute(pickupCoords, dropoffCoords);

        this.mapService.showCarAnimation();
      },
      error: err => {
        console.error('Ne mogu da dohvatim aktivnu voznju', err);
      }
    });
  }

  async geocode(address: string): Promise<[number, number]> {

    const res = await fetch(
      `http://localhost:8080/api/geocode?q=${encodeURIComponent(address)}`
    );
    const data = await res.json();
    return [data.lat, data.lon];
  }

  handleShowOnMap(event: {
    pickupCoords: [number, number];
    dropoffCoords: [number, number];
  }) {
    this.mapService.showSnappedRoute(
      event.pickupCoords,
      event.dropoffCoords
    );
  }
}
