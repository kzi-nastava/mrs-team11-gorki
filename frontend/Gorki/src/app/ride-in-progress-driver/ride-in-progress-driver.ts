import { Component, OnInit } from '@angular/core';
import { MapComponent } from "../map/map";
import { MapService } from '../map/map-service';
import { TrackRideDriver } from '../rides/track-ride-driver/track-ride-driver';
import { DriverRideInProgress } from '../service/driver-ride-in-progress';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';

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
      private cdr: ChangeDetectorRef
    ) {}

  async ngOnInit() {
    this.mapService.clearAll();

    this.rideService.getActiveRideAddresses(1).subscribe({
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
    const url =
      'https://nominatim.openstreetmap.org/search?' +
      new URLSearchParams({
        q: address,
        format: 'json',
        limit: '1',
      });

    const res = await fetch(url);
    const data = await res.json();

    if (!data.length) {
      throw new Error('Address not found');
    }

    return [parseFloat(data[0].lat), parseFloat(data[0].lon)];
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
