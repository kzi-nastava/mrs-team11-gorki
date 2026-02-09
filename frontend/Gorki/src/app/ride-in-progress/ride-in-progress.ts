import { Component, OnInit } from '@angular/core';
import { MapComponent } from "../map/map";
import { TrackRide } from '../rides/track-ride/track-ride';
import { MapService } from '../map/map-service';
import { RideInProgressService } from '../service/passenger-ride-in-progress';

@Component({
  selector: 'app-ride-in-progress',
  standalone: true,
  imports: [MapComponent, TrackRide],
  templateUrl: './ride-in-progress.html',
  styleUrls: ['./ride-in-progress.css'],
})
export class RideInProgress implements OnInit {

  pickupAddress: string = '';
  dropoffAddress: string = '';

  constructor(
    private mapService: MapService,
    private rideService: RideInProgressService
  ) {}

  async ngOnInit() {
    this.mapService.clearAll();

    // Dohvati voznju u toku za putnika (primer id = 2)
    this.rideService.getActiveRideAddresses(4).subscribe({
      next: async ({ pickup, dropoff }) => {
        this.pickupAddress = pickup;
        this.dropoffAddress = dropoff;

        const pickupTextEl = document.getElementById('PickupText');
        const dropoffTextEl = document.getElementById('DropoffText');

        if (pickupTextEl) pickupTextEl.textContent = pickup;
        if (dropoffTextEl) dropoffTextEl.textContent = dropoff;
        

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
